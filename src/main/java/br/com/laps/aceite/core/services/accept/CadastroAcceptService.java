package br.com.laps.aceite.core.services.accept;

import br.com.laps.aceite.api.accepts.assemblers.AcceptAssembler;
import br.com.laps.aceite.api.accepts.dtos.AcceptRequest;
import br.com.laps.aceite.api.accepts.dtos.AcceptResponse;
import br.com.laps.aceite.api.accepts.mappers.AcceptMapper;
import br.com.laps.aceite.api.file.FileManagerController;
import br.com.laps.aceite.core.enums.AceiteStatus;
import br.com.laps.aceite.core.exceptions.NegocioException;
import br.com.laps.aceite.core.models.*;
import br.com.laps.aceite.core.repositories.*;
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

        @Transactional
        public EntityModel<AcceptResponse> salvar(String acceptRequestForm, MultipartFile foto, String destinatario)
                        throws JsonProcessingException {

                // 1) Parse do request
                AcceptRequest acceptRequest = mapper.readValue(acceptRequestForm, AcceptRequest.class);
                String imoTrimmada = acceptRequest.getImo() != null ? acceptRequest.getImo().trim() : null;

                if (imoTrimmada == null || imoTrimmada.isEmpty()) {
                        throw new NegocioException("O IMO do navio é obrigatório.");
                }

                // 2) Usuário logado
                User user = securityService.getCurrentUser();

                // 3) Busca ou Cria o navio
                Vessel vessel;
                if (acceptRequest.getVesselId() != null) {
                        vessel = vesselRepository.findById(acceptRequest.getVesselId())
                                        .orElseThrow(() -> new NegocioException(
                                                        "Navio não encontrado com ID: " + acceptRequest.getVesselId()));
                } else {
                        vessel = vesselRepository.findByImo(imoTrimmada)
                                        .map(v -> {
                                                if (v.getNome() == null || v.getNome().trim().isEmpty()) {
                                                        v.setNome(acceptRequest.getNome());
                                                        if (v.getMmsi() == null)
                                                                v.setMmsi(acceptRequest.getMmsi());
                                                        if (v.getLoa() == 0)
                                                                v.setLoa(acceptRequest.getLoa() != null
                                                                                ? acceptRequest.getLoa()
                                                                                : 0.0);
                                                        if (v.getBoca() == 0)
                                                                v.setBoca(acceptRequest.getBoca() != null
                                                                                ? acceptRequest.getBoca()
                                                                                : 0.0);
                                                        if (v.getDwt() == 0)
                                                                v.setDwt(acceptRequest.getDwt() != null
                                                                                ? acceptRequest.getDwt()
                                                                                : 0.0);
                                                        if (v.getPontal() == 0)
                                                                v.setPontal(acceptRequest.getPontal() != null
                                                                                ? acceptRequest.getPontal()
                                                                                : 0.0);
                                                        if (v.getCalado_max() == 0)
                                                                v.setCalado_max(acceptRequest.getCalado_max() != null
                                                                                ? acceptRequest.getCalado_max()
                                                                                : 0.0);
                                                        if (v.getCategoria() == null)
                                                                v.setCategoria(acceptRequest.getCategoria());
                                                        return vesselRepository.save(v);
                                                }
                                                return v;
                                        })
                                        .orElseGet(() -> {
                                                Vessel novoVessel = new Vessel();
                                                novoVessel.setImo(imoTrimmada);
                                                novoVessel.setNome(acceptRequest.getNome());
                                                novoVessel.setMmsi(acceptRequest.getMmsi());
                                                novoVessel.setLoa(
                                                                acceptRequest.getLoa() != null ? acceptRequest.getLoa()
                                                                                : 0.0);
                                                novoVessel.setBoca(acceptRequest.getBoca() != null
                                                                ? acceptRequest.getBoca()
                                                                : 0.0);
                                                novoVessel.setDwt(
                                                                acceptRequest.getDwt() != null ? acceptRequest.getDwt()
                                                                                : 0.0);
                                                novoVessel.setPontal(acceptRequest.getPontal() != null
                                                                ? acceptRequest.getPontal()
                                                                : 0.0);
                                                novoVessel.setPonte_mfold(acceptRequest.getPonte_mfold() != null
                                                                ? acceptRequest.getPonte_mfold()
                                                                : 0.0);
                                                novoVessel.setMfold_quilha(acceptRequest.getMfold_quilha() != null
                                                                ? acceptRequest.getMfold_quilha()
                                                                : 0.0);
                                                novoVessel.setCalado_max(acceptRequest.getCalado_max() != null
                                                                ? acceptRequest.getCalado_max()
                                                                : 0.0);
                                                novoVessel.setCalado_entrada(acceptRequest.getCaladoEntrada() != null
                                                                ? acceptRequest.getCaladoEntrada()
                                                                : 0.0);
                                                novoVessel.setCalado_saida(acceptRequest.getCaladoSaida() != null
                                                                ? acceptRequest.getCaladoSaida()
                                                                : 0.0);
                                                novoVessel.setCategoria(acceptRequest.getCategoria() != null
                                                                ? acceptRequest.getCategoria()
                                                                : "1");
                                                novoVessel.setFlag(acceptRequest.getFlag());
                                                novoVessel.setUser(user);
                                                return vesselRepository.save(novoVessel);
                                        });
                }

                // Validação manual extra para garantir dados obrigatórios
                if (vessel.getNome() == null || vessel.getNome().trim().isEmpty()) {
                        throw new NegocioException("O nome do navio é obrigatório e não foi encontrado.");
                }

                // 4) Mapeia DTO -> Entity (Accept)
                Accept accept = acceptMapper.toAccept(acceptRequest);
                accept.setUser(user);
                accept.setVessel(vessel);

                accept.setStatus(AceiteStatus.EM_PROCESSAMENTO);
                accept.setCodigo(String.valueOf(System.currentTimeMillis()).substring(4, 13));
                accept.setDataHoraAccept(java.time.LocalDateTime.now());

                // Associa os berços selecionados
                if (acceptRequest.getBercosSelecionados() != null && !acceptRequest.getBercosSelecionados().isEmpty()) {
                        List<Berco> bercos = bercoRepository.findAllById(acceptRequest.getBercosSelecionados());
                        accept.setBercos(bercos);
                }

                // 5) Upload (se tiver foto)
                if (foto != null && !foto.isEmpty()) {
                        accept.setPath(foto.getOriginalFilename());
                        fileManagerController.uploadFile(foto);
                }

                // 6) Salva o Aceite
                accept = acceptRepository.saveAndFlush(accept);

                // Mapeia para Response (o mapper agora lida com as garantias de dados)
                AcceptResponse acceptResponse = acceptMapper.toAcceptResponse(accept);

                return acceptAssembler.toModel(acceptResponse);
        }
}