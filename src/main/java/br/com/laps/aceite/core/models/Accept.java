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
        @Column(nullable = false, length = 7)
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

        @Column(length = 100)
        private String categoria;

        private Double loa;

        private Double dwt;

        @Column(name = "calado_entrada")
        private Double calado_entrada;

        @Column(name = "calado_saida")
        private Double calado_saida;

        @Builder.Default
        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "accept_berco", joinColumns = @JoinColumn(name = "accept_id"), inverseJoinColumns = @JoinColumn(name = "berco_id"))
        private List<Berco> bercos = new ArrayList<>();

        private String mmsi;

        @Column(length = 150)
        private String nome;

        private Double boca;

        private Double pontal;

        @Column(length = 100)
        private String flag;

        @Column(name = "calado_max")
        private Double calado_max;

        @Column(name = "ponte_mfold")
        private Double ponte_mfold;

        @Column(name = "mfold_quilha")
        private Double mfold_quilha;

        @Column(length = 500)
        private String path;
}