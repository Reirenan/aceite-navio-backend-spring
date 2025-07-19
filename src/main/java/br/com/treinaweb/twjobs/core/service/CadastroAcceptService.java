package br.com.treinaweb.twjobs.core.service;


import br.com.treinaweb.twjobs.api.accepts.assemblers.AcceptAssembler;
import br.com.treinaweb.twjobs.api.accepts.dtos.AcceptRequest;
import br.com.treinaweb.twjobs.api.accepts.dtos.AcceptResponse;
import br.com.treinaweb.twjobs.api.accepts.mappers.AcceptMapper;
import br.com.treinaweb.twjobs.api.file.FileManagerController;
import br.com.treinaweb.twjobs.api.vessels.dtos.VesselRequest;
import br.com.treinaweb.twjobs.core.enums.EmailActivation;
import br.com.treinaweb.twjobs.core.enums.VeriStatus;
import br.com.treinaweb.twjobs.core.exceptions.NegocioException;
import br.com.treinaweb.twjobs.core.exceptions.VesselNotFoundException;
import br.com.treinaweb.twjobs.core.models.*;
import br.com.treinaweb.twjobs.core.repositories.*;
import br.com.treinaweb.twjobs.core.services.auth.SecurityService;
//import jakarta.mail.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;



import java.util.HashMap;
import java.util.Map;


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
    private final BlackListRepository blackListRepository;

    private final FileManagerController fileManagerController;

    private final EmailSendRepository emailSendRepository;

    private final UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EmailService emailService;





    @Transactional
    public EntityModel<AcceptResponse> salvar(String acceptRequestForm, MultipartFile foto, String destinatario) throws JsonProcessingException {

        //verifica extensao
        String filename = foto.getOriginalFilename();
        String extension = null;
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex >= 0) {
            extension = filename.substring(dotIndex + 1);
        }

        String[] extensions = {"txt", "zip", "pdf"};

        Boolean verifica = false;
        if(extension!=null) {
            for(String i : extensions){
                if(i.equals(extension) ) {
                    verifica =true;
                    break;
                }
            }

            if(!verifica){
                throw new NegocioException(extension);
            }

        }

       // destinatario = String.valueOf(emailSendRepository.findByStatus(EmailActivation.enable));
        //if you are using optional and want to get or set an attribute, you must use the "get()" before
        destinatario = String.valueOf(userRepository.findBySendEmail(Boolean.TRUE).get().getEmail());

        AcceptRequest acceptRequest = mapper.readValue(acceptRequestForm, AcceptRequest.class);


        User user = securityService.getCurrentUser();
        Long userId = user.getId();

        var accept = acceptMapper.toAccept(acceptRequest);
        accept.setUser(user);

        if(foto!=null) {
            accept.setPath(foto.getOriginalFilename());
            fileManagerController.uploadFile(foto);
        }

//      """
//        Procura navio por imo e userId.
//                   """
//        Vessel vessel = vesselRepository.findByImoAndUserId(accept.getImo(), userId)
//                .orElseThrow(VesselNotFoundException::new);

        Vessel vessel = vesselRepository.findByImo(accept.getImo())
                .orElseThrow(VesselNotFoundException::new);




//        if(vessel.getCategoria()==null&&accept.getCategoria()!=null) {
//            vessel.setCategoria(accept.getCategoria());
//            vesselRepository.save(vessel);
//        }

//      """Setando as datas de alteração"""
        accept.setVessel(vessel);
        accept.setDataAccept(String.valueOf(LocalDate.now()+" "+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        accept.setData_create(String.valueOf(LocalDate.now()+" "+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        accept.setData_update(String.valueOf(LocalDate.now()));

        accept.setTime_accept(String.valueOf(LocalDate.now()+" "+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        accept.setTime_create(String.valueOf(LocalDate.now()+" "+ LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
        accept.setTime_update(String.valueOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));

//      """ Inutilizado por enquanto
//        Accept lastAccepted = acceptRepository.findTop1ByImoOrderByIdDesc(vessel.getImo());


        List<Berco> bercos = bercoRepository.findAll();

        List<Berco> bercosCompativeis = new ArrayList<>();

//        CHECA SE TÁ NA BLACKLIST
        boolean blackListed = blackListRepository.existsByImo(accept.getImo());

//       ~~~    PLANO DE AMARRACAO PORTO(BASEADO NAS REUNIOES)
        for (Berco berco : bercos) {
            String catNavio = accept.getCategoria();   // "2" = granel sólido; "3" = granel líquido; "1" = carga geral
            String catBerco = berco.getCategoria();
            boolean categoriaValida = false;

            // Se o navio for granel sólido ("2"), só aceita berços "1" ou "2"
            if ("2".equals(catNavio)) {
                if ("1".equals(catBerco) || "2".equals(catBerco)) {
                    categoriaValida = true;
                }
            }
            // Se o navio for granel líquido ("3"), só aceita berços "3"
            else if ("3".equals(catNavio)) {
                if ("3".equals(catBerco)) {
                    categoriaValida = true;
                }
            }
            // Se for carga geral ("1") ou outro código, exige igualdade exata
            else {
                if (catNavio.equals(catBerco)) {
                    categoriaValida = true;
                }
            }

            // Verificação de dimensões, DWT e blacklist
            if (categoriaValida
                    && accept.getLoa()           <= berco.getLoa_max()
                    && accept.getCalado_entrada() <= berco.getCalado_max()
                    && accept.getCalado_saida()   <= berco.getCalado_max()
                    && accept.getDwt()           <= berco.getDwt()
                    && !blackListed) {
                bercosCompativeis.add(berco);
            }
        }



        Accept lastAccept = acceptRepository.findFirstByOrderByDataAcceptDesc();

        var lastAcceptId = lastAccept.getId();
        var  currentAcceptId = lastAcceptId +1;

        if (!bercosCompativeis.isEmpty()) {
            accept.setBercos(bercosCompativeis);
//            accept.setStatus(VeriStatus.valueOf("Y"));
            accept.setStatus("Y");
        } else {
//            id, imo, user, status, obs, data de criacao, local hospedagem + URIs
            String msg;
            if(blackListed) {
                msg =       "ID DO ACEITE: "+currentAcceptId+"\n"+
                            "IMO DO NAVIO: "+accept.getImo()+"\n"+
                            "CAUSA IDENTIFICADA(SISTEMA): Navio problemático, está na BLACK LIST!"+"\n"+
                            "STATUS INPUTADO PARA O ACEITE(SISTEMA): Em processamento"+"\n"+
                            "OBS DO USUÁRIO: "+accept.getObs()+"\n"+
                            "DATA CRIAÇÃO DO REGISTRO DE ACEITE: "+accept.getData_create()+"\n"+
                            "DADOS DO USUÁRIO: "+"ID: "+user.getId()+" E-MAIL: "+user.getEmail()+" NOME: "+user.getName()+" PAPÉL: "+user.getRole();
            } else {
                msg =       "ID DO ACEITE: "+currentAcceptId+"\n"+
                            "IMO DO NAVIO: "+accept.getImo()+"\n"+
                            "CAUSA IDENTIFICADA(SISTEMA): Navio problemático, de acordo com categoria, loa, dwt e calados cadastrados, nenhum berço o comporta!"+"\n"+
                            "STATUS INPUTADO PARA O ACEITE(SISTEMA): Em processamento"+"\n"+
                            "OBS DO USUÁRIO: "+accept.getObs()+"\n"+
                            "DATA CRIAÇÃO DO REGISTRO DE ACEITE: "+accept.getData_create()+"\n"+
                            "DADOS DO USUÁRIO: "+"ID: "+user.getId()+" E-MAIL: "+user.getEmail()+" NOME: "+user.getName()+" PAPEL: "+user.getRole();


            }
            emailService.enviarEmailTexto(destinatario, "Aceite de Navio - BLOQUEADO", msg);
//            accept.setStatus(VeriStatus.valueOf("N"));
            accept.setStatus("N");
//            throw new NegocioException("Nenhum berço compatível.");
        }

        accept = acceptRepository.save(accept);
        var acceptResponse = acceptMapper.toAcceptResponse(accept);
        return acceptAssembler.toModel(acceptResponse);

    }

// ~~ ~~ ~~ COLOCAR DENTRO DE SAVE, CASO PRECISE AUTO-ALIMENTAR VESSEL COM OS DADOS DE  ACCEPT
//    @Transactional
//    public EntityModel<AcceptResponse> saveAcceptOrUpdatadeVesselAlso(@Valid @RequestParam(name="AcceptRequestForm") String acceptRequestForm) {
//
//
//
//
//        Accept savedAccept = acceptRepository.save(accept);
//
//        // Atualiza os campos de Vessel, se necessário
//        Vessel vessel = savedAccept.getVessel();
//        if (vessel != null) {
//            boolean updated = false;
//
//            // Verifica e atualiza os campos comuns
//            if (accept.getCommonField1() != null &&
//                    !accept.getCommonField1().equals(vessel.getCommonField1())) {
//                vessel.setCommonField1(accept.getCommonField1());
//                updated = true;
//            }
//
//            if (accept.getCommonField2() != null &&
//                    !accept.getCommonField2().equals(vessel.getCommonField2())) {
//                vessel.setCommonField2(accept.getCommonField2());
//                updated = true;
//            }
//
//            // Salva as alterações em Vessel, se houver
//            if (updated) {
//                vesselRepository.save(vessel);
//            }
//        }
//
//        return savedAccept;
//
//
//    }







//   ESTATÍSTICA ACEITO, NEGADO, EM ANÁLISE
public Map<String, Long> getStatusStatistics() {
    // Obtem os resultados agrupados diretamente do banco
    List<Object[]> results = acceptRepository.countByStatus();

    // Mapa para armazenar a estatística traduzida
    Map<String, Long> statistics = new HashMap<>();

    for (Object[] result : results) {
        String statusCode = (String) result[0]; // Código de status ("1", "2", "3")
        Long count = (Long) result[1];         // Contagem de registros

        // Traduzir o status code para o rótulo correspondente
        String statusLabel = mapStatusCodeToLabel(statusCode);
        statistics.put(statusLabel, count);
    }

    return statistics;
}


    /**
     * Traduz os códigos de status do banco para rótulos legíveis.
     *
     * @param code Código de status (String)
     * @return Rótulo legível (String)
     */
    private String mapStatusCodeToLabel(String code) {
        switch (code) {
            case "1":
                return "aceito";
            case "2":
                return "negado";
            case "3":
                return "em análise";
            default:
                return "desconhecido";
        }
    }

// Json de getStatusStatistics()
//{
//        "aceito": 10,
//        "negado": 5,
//        "em análise": 8
//}




}










//     ~~~    BACKUPS(IMPORTANTE)

//       PLANO DE AMARRACAO PORTO(BASEADO NAS REUNIOES)
//
//        for (Berco berco : bercos) {
//
////               ~  FALTA A VERIFICACAO DAS CATEGORIAS
//            if ((vessel.getLoa()<=berco.getLoa_max())&&(acceptRequest.getCalado_entrada()<=berco.getCalado_max())&&(acceptRequest.getCalado_saida()<=berco.getCalado_max())&&(vessel.getDwt()<=berco.getDwt())) {
//
//                bercosCompativeis.add(berco);
//            }
//        }





//        for (Berco berco : bercos) {
//
//
//                bercosCompativeis.add(berco);
//
//
//        }






//            for (Berco berco : bercos) {
//                if (  vessel.getLoa()!=null
//                        &&berco.getLoa_max()!=null
//                        &&vessel.getDwt()!=null
//                        &&berco.getDwt()!=null
//                        &&vessel.getPontal()!=null
//                        &&berco.getCalado_max()!=null
//                        &&
//                        vessel.getLoa() <= berco.getLoa_max()
//                        && vessel.getDwt() <= berco.getDwt()
//                        && vessel.getPontal() <= berco.getCalado_max()) {
//                    bercosCompativeis.add(berco);
//                }
//            }
//
//
//        // Define status e berço com base nas verificações
//        if (!bercosCompativeis.isEmpty()) {
//            accept.setStatus(VeriStatus.valueOf("Y"));
//            accept.setBercos(bercosCompativeis);
//        } else {
////            Esse status representa que necessita-se de averiguação
//            accept.setStatus(VeriStatus.valueOf("N"));
//            throw new NegocioException("Nenhum berço compatível.");
//        }

//       PLANO DE AMARRACAO EM VIGENCIA DO SISTEMA
//
//        for (Berco berco : bercos) {
//            if (vessel.getLoa() <= berco.getLoa_max()
//                    && vessel.getDwt() <= berco.getDwt()
//            ) {
//                bercosCompativeis.add(berco);
//            }
//        }
//
//
//        if (!bercosCompativeis.isEmpty()) {
//            accept.setStatus(VeriStatus.valueOf("Y"));
//            accept.setBercos(bercosCompativeis);
//        } else {
//            accept.setStatus(VeriStatus.valueOf("N"));
//            throw new NegocioException("Nenhum berço compatível.");
//        }




//        if ((vessel.getLoa()!=0&vessel.getDwt()!=0)) {
//            accept.setStatus(VeriStatus.valueOf("Y"));
//            accept.setBercos(List.of(bercos.get(0)));
//        }
//        accept.setStatus(VeriStatus.valueOf("Y"));
//        accept.setBercos(List.of(bercos.get(0)));


////      """  Berços 99, 100, 101, 102, 103, 104 e 105:
//        if(
//                (vessel.getLoa()<150&vessel.getDwt()<20.000)
//                || ((vessel.getLoa()>150&vessel.getLoa()<190)
//                        &(vessel.getDwt()>20.000&vessel.getDwt()<40.000))
//                    || (vessel.getLoa()>190&vessel.getDwt()>40.000)
//                                                                 ) {
//
//                                    accept.setStatus(VeriStatus.valueOf("Y"));
//                                    accept.setBercos(List.of(bercos.get(0)));
//
//
//        }
//
//
////      """  Berços 106 e 108:
//        else if(
//                    (vessel.getLoa()<190&vessel.getDwt()<40.000)
//                    || (vessel.getLoa()>190&vessel.getDwt()>20.000)
//                                                                 ) {
//
//                                    accept.setStatus(VeriStatus.valueOf("Y"));
//                                    accept.setBercos(List.of(bercos.get(1)));
//
//
//        }
//
//
////      """  GLP:
//        else if(
//                   (vessel.getCategoria()=="2"&vessel.getLoa()<100&vessel.getDwt()<10.000)
//                   || (vessel.getCategoria()=="2"&vessel.getLoa()>100&vessel.getDwt()>10.000)
//                                                              ) {
//
//                                    accept.setStatus(VeriStatus.valueOf("Y"));
//                                    accept.setBercos(List.of(bercos.get(2)));
//
//
////      """  CONTAINERS:
//        } else if(
//                    (vessel.getLoa()<150&vessel.getDwt()<20.000)
//                    || ((vessel.getLoa()>150&vessel.getLoa()<180)
//                            &(vessel.getDwt()>20.000&vessel.getDwt()<40.000))
//                         || (vessel.getLoa()>180&vessel.getDwt()>40.000)
//                                                                         ) {
//
//                                    throw new NegocioException("Nenhum container cadastrado.");
//
//
//        }
//
//
//
//
//        else {
//
//
//                                    throw new NegocioException("Nenhum berço compatível.");
//
//
//        }


//        if(
//                (vessel.getLoa()>150&vessel.getDwt()>20.000)
//        ) {
//            accept.setStatus(VeriStatus.valueOf("Y"));
//            accept.setBercos(List.of(bercos.get(0),bercos.get(1)));
//
//
//        }
//        accept.setStatus(VeriStatus.valueOf("N"));