package br.com.treinaweb.twjobs.api.aceites.assemblers;//package br.com.treinaweb.twjobs.api.ships.assemblers;

import br.com.treinaweb.twjobs.api.aceites.controllers.AceiteRestController;
import br.com.treinaweb.twjobs.api.aceites.dtos.AceiteResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AceiteAssembler implements SimpleRepresentationModelAssembler<AceiteResponse> {

    @Override
    public void addLinks(EntityModel<AceiteResponse> resource) {
        var id = resource.getContent().getId();

        var selfLink = linkTo(methodOn(AceiteRestController.class).findById(id))
            .withSelfRel()
            .withType("GET");

        var updateLink = linkTo(methodOn(AceiteRestController.class).update(null, id))
            .withRel("update")
            .withType("PUT");

        var deleteLink = linkTo(methodOn(AceiteRestController.class).delete(id))
            .withRel("delete")
            .withType("DELETE");


        resource.add(selfLink, updateLink, deleteLink);
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<AceiteResponse>> resources) {
        var selfLink = linkTo(methodOn(AceiteRestController.class).findAll(null))
            .withSelfRel()
            .withType("GET");

        var createLink = linkTo(methodOn(AceiteRestController.class).create(null))
            .withRel("create")
            .withType("POST");

        resources.add(selfLink, createLink);
    }

}
