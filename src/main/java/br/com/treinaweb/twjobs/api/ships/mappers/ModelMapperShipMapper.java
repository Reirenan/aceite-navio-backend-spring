//package br.com.treinaweb.twjobs.api.ships.mappers;
//
//import br.com.treinaweb.twjobs.api.ships.dtos.ShipRequest;
//import br.com.treinaweb.twjobs.api.ships.dtos.ShipResponse;
//import br.com.treinaweb.twjobs.core.models.Skill;
//import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class ModelMapperSkillMapper implements SkillMapper {
//
//    private final ModelMapper modelMapper;
//
//    @Override
//    public Skill toSkill(ShipRequest skillRequest) {
//        return modelMapper.map(skillRequest, Skill.class);
//    }
//
//    @Override
//    public ShipResponse toSkillResponse(Skill skill) {
//        return modelMapper.map(skill, ShipResponse.class);
//    }
//
//}
