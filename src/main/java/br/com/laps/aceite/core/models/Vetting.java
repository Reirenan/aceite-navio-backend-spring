package br.com.laps.aceite.core.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
/* MESMA COISA DA BLACKLIST*/
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Vetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // IMO (7 dígitos) - consistente com Accept
    @Column(nullable = false, length = 7)
    private String imo;

    @Column(nullable = false, length = 2000)
    private String reason;

    @Column(name = "datetime_blacklist_create", nullable = false)
    private LocalDateTime datetimeBlacklistCreate;

    private String mmsi;

    @Column(length = 150)
    private String nome;

    // Dados do navio
    @Column(nullable = false)
    private Double loa;

    private Double boca;

    @Column(nullable = false)
    private Double dwt;

    private Double pontal;

    @Column(name = "ponte_mfold")
    private Double ponteMfold;

    @Column(name = "mfold_quilha")
    private Double mfoldQuilha;

    @Column(nullable = false, length = 100)
    private String categoria;

    private String flag;

    @Column(nullable = false)
    private Double caladoEntrada;

    @Column(nullable = false)
    private Double caladoSaida;
}