package br.com.treinaweb.twjobs.core.validators;

import br.com.treinaweb.twjobs.core.repositories.BercoRepository;
import br.com.treinaweb.twjobs.core.services.http.HttpService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BercoNameIsUniqueValidator implements ConstraintValidator<BercoNameIsUnique, Long> {


    private final HttpService httpService;
    private final BercoRepository bercoRepository;

//    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        var id = httpService.getPathVariable("id", Long.class);

        if (id.isEmpty()) {
            return !bercoRepository.existsByNome(value);
        }
        return !bercoRepository.existsByNomeAndIdNot(value, id.get());
    }

 /*   @Override
    public boolean isValid(Long aLong, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }*/
}
