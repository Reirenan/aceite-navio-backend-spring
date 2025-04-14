package br.com.treinaweb.twjobs.core.models;


import br.com.treinaweb.twjobs.core.validators.ValidationGroups;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Getter
@Setter
public class Navio {

    @NotNull(groups = ValidationGroups.NavioId.class)
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long imo;
    @NotBlank
    @NotNull
    @Size(max=60)
    private String mmsi;
    @NotBlank
    @NotNull
    @Size(max=50)
    private String nome;
    @NotNull
    private float loa;
    @NotNull
    private float boca;
    @NotNull
    private float dwt;
    @NotNull
    private float pontal;
    @NotNull
    private float ponte_mfold;
    @NotNull
    private float mfold_quilha;
    @NotNull
    private char categoria;
    @NotNull
    private int flag;
    @NotBlank
    @NotNull
    private String obs;
    @NotNull
    private char status;




}
