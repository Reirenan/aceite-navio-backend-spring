package br.com.treinaweb.twjobs.core.validators;

import br.com.treinaweb.twjobs.core.repositories.SkillRepository;
import br.com.treinaweb.twjobs.core.repositories.VesselRepository;
import br.com.treinaweb.twjobs.core.services.http.HttpService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VesselImoisUniqueValidator implements ConstraintValidator<VesselImoIsUnique, Long> {

    private final HttpService httpService;
    private final VesselRepository vesselRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        var id = httpService.getPathVariable("id", Long.class);

        if (id.isEmpty() ) {
            return !vesselRepository.existsByImo(value);
        }
        return !vesselRepository.existsByImoAndIdNot(value, id.get());

       /* var imo = httpService.getPathVariable("imo", Long.class);

        if (imo.isEmpty()) {
            return !vesselRepository.existsByImo(value);
        }
        return !vesselRepository.existsByImoAndIdNot(value, imo.get());*/
    }



}
