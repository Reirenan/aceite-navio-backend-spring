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

                // 2) Usuário logado
                User user = securityService.getCurrentUser();

                // 3) Mapeia DTO -> Entity
                Accept accept = acceptMapper.toAccept(acceptRequest);
                accept.setUser(user);
                accept.setStatus(AceiteStatus.EM_PROCESSAMENTO);
                accept.setCodigo(String.valueOf(System.currentTimeMillis()).substring(4, 13));

                // 4) Upload (se tiver foto)
                if (foto != null && !foto.isEmpty()) {
                        accept.setPath(foto.getOriginalFilename());
                        fileManagerController.uploadFile(foto);
                }

                // 5) Vessel por IMO
                Vessel vessel = vesselRepository.findByImo(accept.getImo())
                                .orElseThrow(VesselNotFoundException::new);
                accept.setVessel(vessel);
                accept.setDataHoraAccept(java.time.LocalDateTime.now());

                // 6) Salva
                accept = acceptRepository.save(accept);

                AcceptResponse acceptResponse = acceptMapper.toAcceptResponse(accept);
                return acceptAssembler.toModel(acceptResponse);
        }
}