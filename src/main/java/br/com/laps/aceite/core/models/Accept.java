package br.com.treinaweb.twjobs.core.models;

import br.com.treinaweb.twjobs.core.enums.AceiteStatus;
import br.com.treinaweb.twjobs.core.enums.CategoriaVessel;
import br.com.treinaweb.twjobs.core.enums.VeriStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.Modifying;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded=true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Accept {

    //   TESTAR

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @ManyToOne
    private User user;


    //    NOT NULL
    @ToString.Include
    @ManyToOne(cascade = CascadeType.ALL)
    private Vessel vessel;

    //    NOT NULL
    @Column( nullable = false, length = 50)
    private Long imo;

    //    < NOT NULL
    @Column(name="data_accept", nullable = false, length = 100)
    private String dataAccept;
    @Column(nullable = false, length = 100)
    private String data_create;
    @Column(nullable = false, length = 100)
    private String data_update;// >

    @Column(nullable = false, length = 100)
    private String time_accept;
    @Column(nullable = false, length = 100)
    private String time_create;
    @Column(nullable = false, length = 100)
    private String time_update;


    private String obs;
    private String restricoes;

    @Column( columnDefinition = "varchar(9) DEFAULT 0", nullable = true, length = 9)
    private String codigo;

//    @Column(nullable = false, length = 20)
//    @Enumerated(EnumType.STRING)
//    private AceiteStatus status;

    //    NOT NULL
    //    Y -> 1; NE -> 2; N-> 3
    @Column( columnDefinition = "CHAR(50) DEFAULT 'N'", nullable = false, length = 50)
//    @Enumerated(EnumType.STRING)
    private String status;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name="accept_berco",
            joinColumns = @JoinColumn(name="accept_id"),
            inverseJoinColumns = @JoinColumn(name="berco_id")
    )
    @JsonIgnoreProperties("accept")
    private List<Berco> bercos;
//  Assim, em name eu defino o nome da tabela intermediária.
//  Em joiColumns em digo o nome da tabela mais forte.]
//  E em inverseJoinColumns eu defino o nome da outra tabela que irá compor accept_berco.

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

    @Column(length=500)
    private String path;

    @Column(nullable = false)
    private List<Long> bercosSelecionados = new ArrayList<>();

//    @Column(nullable = false, length = 20)
//    @Enumerated(EnumType.STRING)
//    private AceiteStatus status;


}
