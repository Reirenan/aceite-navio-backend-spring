package br.com.laps.aceite.core.models;

import br.com.laps.aceite.core.enums.VeriStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Vessel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String imo;

    @Column(length = 20)
    private String mmsi;

    @Column(length = 150)
    private String nome;

    @Column(nullable = false)
    private double loa;

    private double boca;

    @Column(nullable = false)
    private double dwt;

    private double pontal;

    private double ponte_mfold;

    private double mfold_quilha;

    @Column(nullable = false, length = 100)
    private String categoria;

    @Column(length = 255)
    private String flag;

    @Column(length = 1000)
    private String obs;

    private double calado_entrada;

    private double calado_saida;

    @Column(nullable = false)
    private double calado_max;

    @CreationTimestamp
    @Column(name = "data_create", updatable = false, nullable = false)
    private LocalDateTime dataCreate;

    @Column(length = 500)
    private String path;
}