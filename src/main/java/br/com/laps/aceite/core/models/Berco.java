package br.com.laps.aceite.core.models;

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
public class  Berco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    private Double compriEstrutural;
    private Double compriUtil;
    private Double dwt;
    private Double largura;
    private Double profundidade;
    private Double caladoMax;
    private Double bocaMax;
    private Double loaMax;

    @ManyToMany(mappedBy = "bercos", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties("bercos")
    private List<Accept> accepts;

    @Column( nullable = false, length = 255)
    private String categoria;
}
