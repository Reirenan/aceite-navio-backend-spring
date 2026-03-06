package br.com.laps.aceite.core.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Vetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Include
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 7)
    private String imo;

    @Column(nullable = false, length = 2000)
    private String reason;

    @CreationTimestamp
    @Column(name = "data_create", updatable = false, nullable = false)
    private LocalDateTime dataCreate;

    private String mmsi;

    @Column(length = 150)
    private String nome;

    private String flag;
}