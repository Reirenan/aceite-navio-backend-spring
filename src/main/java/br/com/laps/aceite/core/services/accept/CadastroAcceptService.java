package br.com.laps.aceite.core.services.accept;

import br.com.laps.aceite.api.accepts.assemblers.AcceptAssembler;
import br.com.laps.aceite.api.accepts.dtos.AcceptRequest;
import br.com.laps.aceite.api.accepts.dtos.AcceptResponse;
import br.com.laps.aceite.api.accepts.mappers.AcceptMapper;
import br.com.laps.aceite.api.file.FileManagerController;
import br.com.laps.aceite.core.enums.AceiteStatus;
import br.com.laps.aceite.core.exceptions.NegocioException;
import br.com.laps.aceite.core.exceptions.VesselNotFoundException;
import br.com.laps.aceite.core.models.*;
import br.com.laps.aceite.core.repositories.*;
import br.com.laps.aceite.core.services.audit.Auditable;
import br.com.laps.aceite.core.services.auth.SecurityService;
import br.com.laps.aceite.core.services.email.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CadastroAcceptService {

        private final AcceptMapper acceptMapper;
        private final AcceptAssembler acceptAssembler;
        private final AcceptRepository acceptRepository;
        private final SecurityService securityService;
        private final PagedResourcesAssembler<AcceptResponse> pagedResourcesAssembler;

        private final VesselRepository vesselRepository;
        private final BercoRepository bercoRepository;
        private final VettingRepository vettingRepository;

        private final FileManagerController fileManagerController;

        private final UserRepository userRepository;

        @Autowired
        private ObjectMapper mapper;

        @Autowired
        private EmailService emailService;

        @Auditable(entity = "Accept", clazz = Accept.class)
        @Transactional
        public EntityModel<AcceptResponse> salvar(String acceptRequestForm, MultipartFile foto, String destinatario)
                        throws JsonProcessingException {

                // 1) Parse do request
                AcceptRequest acceptRequest = mapper.readValue(acceptRequestForm, AcceptRequest.class);

                // 2) Validação de arquivo (extensão)
                if (foto != null && !foto.isEmpty()) {
                        String filename = foto.getOriginalFilename();
                        if (filename == null || (!filename.endsWith(".txt") && !filename.endsWith(".zip")
                                        && !filename.endsWith(".pdf"))) {
                                throw new NegocioException(
                                                "Extensão de arquivo inválida. Apenas .txt, .zip e .pdf são permitidos.");
                        }
                }

                // 3) Usuário logado
                User user = securityService.getCurrentUser();

                // 4) Vessel por IMO (Verificar existência)
                Vessel vessel = vesselRepository.findByImo(acceptRequest.getImo())
                                .orElseThrow(() -> new NegocioException("Navio não encontrado com o IMO informado."));

                // 5) Mapeia DTO -> Entity e dados básicos
                Accept accept = acceptMapper.toAccept(acceptRequest);
                accept.setUser(user);
                accept.setVessel(vessel);
                accept.setDataHoraAccept(java.time.LocalDateTime.now());
                accept.setCodigo(String.valueOf(System.currentTimeMillis()).substring(4, 13));

                // Copia campos específicos do request para o accept (caso o mapper não tenha
                // feito)
                accept.setCategoria(acceptRequest.getCategoria());
                accept.setLoa(acceptRequest.getLoa());
                accept.setDwt(acceptRequest.getDwt());
                accept.setCalado_entrada(acceptRequest.getCalado_entrada());
                accept.setCalado_saida(acceptRequest.getCalado_saida());

                // Novos campos para snapshot
                accept.setMmsi(acceptRequest.getMmsi());
                accept.setNome(acceptRequest.getNome());
                accept.setBoca(acceptRequest.getBoca());
                accept.setPontal(acceptRequest.getPontal());
                accept.setFlag(acceptRequest.getFlag());
                accept.setCalado_max(acceptRequest.getCalado_max());
                accept.setPonte_mfold(acceptRequest.getPonte_mfold());
                accept.setMfold_quilha(acceptRequest.getMfold_quilha());

                // 6) Verificação de Blacklist (Vetting) - PRIORIDADE MÁXIMA
                if (vettingRepository.existsByImo(acceptRequest.getImo())) {
                        accept.setStatus(AceiteStatus.BLOQUEADO);
                        accept = acceptRepository.save(accept);

                        emailService.enviarEmailTexto(user.getEmail(),
                                        "Aceite Bloqueado - Blacklist",
                                        "O aceite para o navio " + vessel.getNome() + " (IMO: " + accept.getImo()
                                                        + ") foi BLOQUEADO pois o navio está na blacklist.");

                        return acceptAssembler.toModel(acceptMapper.toAcceptResponse(accept));
                }

                // 7) Upload (se tiver foto)
                if (foto != null && !foto.isEmpty()) {
                        accept.setPath(foto.getOriginalFilename());
                        fileManagerController.uploadFile(foto);
                }

                // 8) Determinação de Berços e Status
                List<Berco> bercosAfetados = new java.util.ArrayList<>();

                if (acceptRequest.getBercosSelecionados() != null && !acceptRequest.getBercosSelecionados().isEmpty()) {
                        // Restrição Manual
                        for (Long bercoId : acceptRequest.getBercosSelecionados()) {
                                bercoRepository.findById(bercoId).ifPresent(bercosAfetados::add);
                        }
                        accept.setStatus(AceiteStatus.RESTRICAO_MANUAL);
                        emailService.enviarEmailTexto(user.getEmail(),
                                        "Aceite com Restrição Manual",
                                        "O aceite para o navio " + vessel.getNome()
                                                        + " requer análise humana devido à seleção manual de berços.");
                } else {
                        // Aceite Automático (Compatibilidade Técnica)
                        bercosAfetados = obterBercosCompativeis(accept);

                        if (!bercosAfetados.isEmpty()) {
                                accept.setStatus(AceiteStatus.ACEITO);
                                emailService.enviarEmailTexto(user.getEmail(),
                                                "Aceite Aprovado Automaticamente",
                                                "O aceite para o navio " + vessel.getNome()
                                                                + " foi APROVADO AUTOMATICAMENTE devido à compatibilidade técnica com os berços: "
                                                                + bercosAfetados.stream().map(Berco::getNome)
                                                                                .collect(Collectors.joining(", "))
                                                                + ".");
                        } else {
                                accept.setStatus(AceiteStatus.EM_PROCESSAMENTO);
                        }
                }

                accept.setBercos(bercosAfetados);

                // 9) Salva
                accept = acceptRepository.save(accept);

                AcceptResponse acceptResponse = acceptMapper.toAcceptResponse(accept);
                return acceptAssembler.toModel(acceptResponse);
        }

        public List<Berco> obterBercosCompativeis(Accept accept) {
                List<Berco> todosBercos = bercoRepository.findAll();
                List<Berco> compativeis = new ArrayList<>();
                for (Berco berco : todosBercos) {
                        if (isBercoCompativel(accept, berco)) {
                                compativeis.add(berco);
                        }
                }
                return compativeis;
        }

        private boolean isBercoCompativel(Accept accept, Berco berco) {
                // Regras: Categoria igual, LOA <= LOA Max, Calados <= Calado Max, DWT <= DWT
                // Max

                // Normaliza a categoria do aceite (converte "1", "2", "3" para os nomes por
                // extenso)
                String categoriaAceite = accept.getCategoria();
                if (categoriaAceite != null) {
                        if (categoriaAceite.equals("1"))
                                categoriaAceite = "Granel Sólido";
                        else if (categoriaAceite.equals("2"))
                                categoriaAceite = "Granel Líquido";
                        else if (categoriaAceite.equals("3"))
                                categoriaAceite = "Carga Geral";
                }
                System.out.println("Categoria do Aceite: " + categoriaAceite);
                System.out.println("Categoria do Berço: " + berco.getCategoria());
                boolean categoriaCompativel = berco.getCategoria() != null && categoriaAceite != null
                                && berco.getCategoria().trim().equalsIgnoreCase(categoriaAceite.trim());
                boolean loaCompativel = accept.getLoa() != null && berco.getLoaMax() != null
                                && accept.getLoa() <= berco.getLoaMax();
                boolean caladoEntradaCompativel = accept.getCalado_entrada() != null && berco.getCaladoMax() != null
                                && accept.getCalado_entrada() <= berco.getCaladoMax();
                boolean caladoSaidaCompativel = accept.getCalado_saida() != null && berco.getCaladoMax() != null
                                && accept.getCalado_saida() <= berco.getCaladoMax();
                boolean dwtCompativel = accept.getDwt() != null && berco.getDwt() != null
                                && accept.getDwt() <= berco.getDwt();
                boolean bocaCompativel = accept.getBoca() != null && berco.getBocaMax() != null
                                && accept.getBoca() <= berco.getBocaMax();

                return categoriaCompativel && loaCompativel && caladoEntradaCompativel && caladoSaidaCompativel
                                && dwtCompativel && bocaCompativel;
        }
}