package br.com.treinaweb.twjobs.core.models;

import br.com.treinaweb.twjobs.core.enums.AceiteStatus;
import br.com.treinaweb.twjobs.core.enums.CategoriaVessel;
import br.com.treinaweb.twjobs.core.enums.VeriStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

@Data
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Vessel {

    //   TESTAR

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Include
    @ManyToOne
    private User user;

//    NOT NULL
    @Column( nullable = false, length = 50)
    //@Column(nullable = false, unique = true, length = 50)
    private Long imo;

    private String mmsi;

    private String nome;

//    NOT NULL
    @Column( nullable = false, length = 100)
    private Float loa;

    private Float boca;

//    NOT NULL
    @Column( nullable = false, length = 100)
    private Float dwt;

    private Float pontal;

    private Float ponte_mfold;

    private Float mfold_quilha;

//    NOT NULL
    @Column( nullable = false, length = 100)
    private String categoria;

//    private Integer flag;

    @Column(nullable = true, length = 255)
    private String flag;

    private String obs;

    @Column(nullable = true, length = 10)
    private Float calado_entrada;

    @Column(nullable = true, length = 10)
    private Float calado_saida;

    @Column(nullable = false, length = 10)
    private Float calado_max;

    @CreationTimestamp
    @Column(name = "data_create", updatable = false)
    private LocalDateTime dataCreate;


//    @Column(nullable = false, length = 20)
//    @Enumerated(EnumType.STRING)
//    private AceiteStatus status;

    //    CAN BE NULL
//    @Column( columnDefinition = "ENUM('Y','NE','N') DEFAULT 'N'", nullable = true, length = 20)
//    @Enumerated(EnumType.STRING)
    @Column( columnDefinition = "CHAR(50) DEFAULT 'N'", nullable = true, length = 50)
    private String status;

//   DELETAR TABELA
    @Column( columnDefinition = "ENUM('Y','NE','N') DEFAULT 'N'", nullable = true, length = 20)
    @Enumerated(EnumType.STRING)
    private VeriStatus st_ver_vessel;


    @Column(length=500)
    private String path;

}
