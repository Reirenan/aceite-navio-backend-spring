package br.com.treinaweb.twjobs.core.models;

import br.com.treinaweb.twjobs.core.enums.CategoriaVessel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Berco {

    //   AJEITAR


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private Long nome;

    private Long compri_estrutural;
    private Long compri_util;
    private Float dwt;
    private Long largura;
    private Long profundidade;
    private Float calado_max;
    private Float boca_max;
    private Float loa_max;

    @ManyToMany(mappedBy = "bercos", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("bercos")
    private List<Accept> accepts;
//    Irá procurar uma propriedade chamada books dentro da classe Accepts.
//    E fará a ligação entre as duas.
//  O JsonIgnoreProperties serve para evitar uma

//    @Column( columnDefinition = "ENUM('G_LIQUIDO','G_SOLIDO','CARGA_GERAL')", nullable = true, length = 50)
//    @Enumerated(EnumType.STRING)
//    private CategoriaVessel categoria;

    @Column( nullable = false, length = 255)
    private String categoria;

}
