package br.com.laps.aceite.core.models;

import br.com.laps.aceite.core.enums.AceiteStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Accept {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ToString.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vessel_id", nullable = false)
    private Vessel vessel;

    // IMO (7 dígitos)
    @Column(nullable = false, length = 7, unique = true)
    private String imo;

    // Data do aceite
    @Column(name = "data_accept", nullable = false)
    private LocalDateTime dataHoraAccept;

    @Column(length = 2000)
    private String obs;

    @Column(length = 2000)
    private String restricoes;

    @Column(nullable = false, length = 9, columnDefinition = "varchar(9) default '0'")
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AceiteStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "accept_berco",
            joinColumns = @JoinColumn(name = "accept_id"),
            inverseJoinColumns = @JoinColumn(name = "berco_id")
    )
    @JsonIgnoreProperties("accept")
    private List<Berco> bercos = new ArrayList<>();

    // Lista de IDs separados (caso realmente precise manter)
    @ElementCollection
    @CollectionTable(
            name = "accept_bercos_selecionados",
            joinColumns = @JoinColumn(name = "accept_id")
    )
    @Column(name = "berco_id", nullable = false)
    private List<Long> bercosSelecionados = new ArrayList<>();

    // Dados do navio
    @Column(length = 9)
    private String mmsi;

    @Column(length = 150)
    private String nome;

    @Column(nullable = false)
    private Double loa;

    private Double boca;

    @Column(nullable = false)
    private Double dwt;

    private Double pontal;

    private Double ponteMfold;

    private Double mfoldQuilha;

    @Column(nullable = false, length = 100)
    private String categoria;

    private Integer flag;

    @Column(nullable = false)
    private Double caladoEntrada;

    @Column(nullable = false)
    private Double caladoSaida;

    @Column(length = 500)
    private String path;
}