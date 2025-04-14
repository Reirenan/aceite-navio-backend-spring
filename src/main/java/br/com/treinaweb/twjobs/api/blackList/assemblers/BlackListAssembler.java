package br.com.treinaweb.twjobs.api.blackList.assemblers;//package br.com.treinaweb.twjobs.api.ships.assemblers;

import br.com.treinaweb.twjobs.api.blackList.controllers.BlackListRestController;
import br.com.treinaweb.twjobs.api.blackList.dtos.BlackListResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BlackListAssembler implements SimpleRepresentationModelAssembler<BlackListResponse> {

    @Override
    public void addLinks(EntityModel<BlackListResponse> resource) {
        var id = resource.getContent().getId();

//        var selfLink = linkTo(methodOn(AcceptRestController.class).findById(id))
//            .withSelfRel()
//            .withType("GET");

//        var updateLink = linkTo(methodOn(BlackListRestController.class).update(null, id))
//            .withRel("update")
//            .withType("PUT");
//
//        var deleteLink = linkTo(methodOn(BlackListRestController.class).delete(id))
//            .withRel("delete")
//            .withType("DELETE");


        resource.add(
//                selfLink,
//                updateLink
//                ,
//                deleteLink
        );
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<BlackListResponse>> resources) {
        var selfLink = linkTo(methodOn(BlackListRestController.class).findAll(null))
            .withSelfRel()
            .withType("GET");

        var createLink = linkTo(methodOn(BlackListRestController.class).create(null))
            .withRel("create")
            .withType("POST");

        resources.add(selfLink, createLink);
    }

}
