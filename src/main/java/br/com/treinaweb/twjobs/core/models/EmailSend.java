package br.com.treinaweb.twjobs.core.models;

import br.com.treinaweb.twjobs.core.enums.EmailActivation;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded=true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EmailSend {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Include
    @ManyToOne
    private User user;

    @Column(nullable = false, length = 100)
    private String content;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EmailActivation status;


}




