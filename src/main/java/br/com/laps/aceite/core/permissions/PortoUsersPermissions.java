package br.com.laps.aceite.core.permissions;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface PortoUsersPermissions {

    // ================================
    // AGENTE_NAVIO
    // ================================
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('ROLE_AGENTE_NAVIO')")
    @interface IsAgenteNavio {}


    // ================================
    // FUNCIONARIO_COACE
    // ================================
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('ROLE_FUNCIONARIO_COACE')")
    @interface IsFuncionarioCoace {}


    // ================================
    // ADMINISTRADOR
    // ================================
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
    @interface IsAdministrador {}
}