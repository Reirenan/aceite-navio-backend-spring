package br.com.treinaweb.twjobs.api.users.controllers;

import br.com.treinaweb.twjobs.api.accepts.dtos.AcceptResponse;
import br.com.treinaweb.twjobs.api.users.assemblers.UserAssembler;
import br.com.treinaweb.twjobs.api.users.dtos.UserResponse;
import br.com.treinaweb.twjobs.api.users.mappers.UserMapper;
import br.com.treinaweb.twjobs.api.vessels.assemblers.VesselAssembler;
import br.com.treinaweb.twjobs.api.vessels.mappers.VesselMapper;
import br.com.treinaweb.twjobs.core.exceptions.NegocioException;
import br.com.treinaweb.twjobs.core.models.User;
import br.com.treinaweb.twjobs.core.permissions.TWJobsPermissions;
import br.com.treinaweb.twjobs.core.repositories.UserRepository;
import br.com.treinaweb.twjobs.core.services.auth.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersCRUDRestController {

//    private final UserMapper userMapper;


    private final UserAssembler userAssembler;
    private final UserMapper userMapper;

    private final UserRepository userRepository;

    private final SecurityService securityService;

    private final PasswordEncoder passwordEncoder;

//    private final UserAssembler userAssembler;

    private final PagedResourcesAssembler<UserResponse> pagedResourcesAssembler;

    @GetMapping
    @TWJobsPermissions.IsCompany
    public CollectionModel<EntityModel<UserResponse>> findAll(@PageableDefault(value = 15) Pageable pageable) {

        var users = userRepository.findAll(pageable)
                .map(userMapper::toUserResponse);
        return pagedResourcesAssembler.toModel(users, userAssembler);
    }

    @PutMapping("/{id}")
    @TWJobsPermissions.IsCompany
    public User update(@PathVariable Long id, @RequestBody User user) {

        Optional<User> userSaved = userRepository.findById(id);

        //A única coisa que pode alterar até agora é o atributo SendEmail
        user.setEmail(userSaved.get().getEmail());
        user.setPassword(userSaved.get().getPassword());
        user.setName(userSaved.get().getName());
        user.setRole(userSaved.get().getRole());

        user.setId(id);

        Boolean check = userRepository.existsBySendEmail(user.getSendEmail());

        if(user.getSendEmail() == Boolean.TRUE) {
            if(check) {
                user.setSendEmail(Boolean.FALSE);
                throw new NegocioException("Você só pode ter 1 e-mail ativo");
            }
        }

        //É NECESSÁRIO QUE O FRONT DESCRIPTOGRAFE
        //var passwordHash = passwordEncoder.encode(user.getPassword());
        //user.setPassword(passwordHash);

        return userRepository.save(user);

    }

    @DeleteMapping("{id}")
    @TWJobsPermissions
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Boolean check = userRepository.existsById(id);
        Optional<User> user = userRepository.findById(id);

        if(check) {

            if(user != null) {
                //user.deleteById(id);
                throw new NegocioException("Error");
            }

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}

