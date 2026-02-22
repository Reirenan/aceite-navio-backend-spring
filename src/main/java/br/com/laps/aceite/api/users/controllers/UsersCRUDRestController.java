package br.com.laps.aceite.api.users.controllers;

import br.com.laps.aceite.api.users.assemblers.UserAssembler;
import br.com.laps.aceite.api.users.dtos.UserResponse;
import br.com.laps.aceite.api.users.mappers.UserMapper;
import br.com.laps.aceite.core.exceptions.NegocioException;
import br.com.laps.aceite.core.models.User;
import br.com.laps.aceite.core.permissions.PortoUsersPermissions;
import br.com.laps.aceite.core.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersCRUDRestController {

    private final UserAssembler userAssembler;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PagedResourcesAssembler<UserResponse> pagedResourcesAssembler;

    @GetMapping
    @PortoUsersPermissions.IsAdministrador
    public CollectionModel<EntityModel<UserResponse>> findAll(@PageableDefault(size = 15) Pageable pageable) {
        Pageable sorted = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("id").descending()
        );

        var users = userRepository.findAll(sorted).map(userMapper::toUserResponse);
        return pagedResourcesAssembler.toModel(users, userAssembler);
    }

    @PostMapping
    @PortoUsersPermissions.IsAdministrador
    public ResponseEntity<UserResponse> create(@RequestBody User user) {
        boolean emailJaExiste = userRepository.existsByEmail(user.getEmail());
        if (emailJaExiste) {
            throw new NegocioException("Já existe um usuário com este e-mail.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var saved = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserResponse(saved));
    }

    @PutMapping("/{id}")
    @PortoUsersPermissions.IsAdministrador
    public ResponseEntity<UserResponse> update(@PathVariable Long id, @RequestBody User user) {
        var userSaved = userRepository.findById(id)
                .orElseThrow(() -> new NegocioException("Usuário não encontrado."));

        user.setId(id);
        user.setEmail(userSaved.getEmail());
        user.setRole(userSaved.getRole());
        user.setName(userSaved.getName());
        user.setPassword(userSaved.getPassword());

        if (Boolean.TRUE.equals(user.getSendEmail())) {
            boolean jaTemOutroAtivo = userRepository.existsBySendEmailTrueAndIdNot(id);
            if (jaTemOutroAtivo) {
                user.setSendEmail(Boolean.FALSE);
                throw new NegocioException("Você só pode ter 1 e-mail ativo.");
            }
        }

        var saved = userRepository.save(user);
        return ResponseEntity.ok(userMapper.toUserResponse(saved));
    }

    @DeleteMapping("/{id}")
    @PortoUsersPermissions.IsAdministrador
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}