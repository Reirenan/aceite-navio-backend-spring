package br.com.treinaweb.twjobs.core.models;


import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BlackList {

    //   TESTAR


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Include
    @ManyToOne
    private User user;

//    NÃO COLOCAREMOS RELACIONAMENTO COM VESSEL PRA EVITAR PRENDÊ-LO(POR ENQUANTO)
    @Column( nullable = false, length = 50)
    private Long imo;

    @Column( nullable = false)
    private String reason;

    @Column(nullable = false, length = 100)
    private String data_blacklisted;
    @Column(nullable = false, length = 100)
    private String data_create;
    @Column(nullable = false, length = 100)
    private String data_update;

    @Column(nullable = false, length = 100)
    private String time_blacklisted;
    @Column(nullable = false, length = 100)
    private String time_create;
    @Column(nullable = false, length = 100)
    private String time_update;

    //    Dados navio
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

    private Integer flag;

    //    NOT NULL
    @Column(nullable = false, length = 10)
    private Float calado_entrada;

    //    NOT NULL
    @Column(nullable = false, length = 10)
    private Float calado_saida;


}
