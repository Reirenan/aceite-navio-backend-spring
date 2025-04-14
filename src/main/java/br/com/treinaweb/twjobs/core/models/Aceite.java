package br.com.treinaweb.twjobs.core.models;

import br.com.treinaweb.twjobs.core.enums.AceiteStatus;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Aceite {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @ManyToOne
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    private Vessel vessel;

    private Long imo;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AceiteStatus status;

}
