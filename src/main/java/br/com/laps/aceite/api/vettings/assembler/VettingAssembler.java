package br.com.laps.aceite.api.vettings.assembler;

import br.com.laps.aceite.api.vettings.controllers.VettingRestController;
import br.com.laps.aceite.api.vettings.dtos.VettingResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VettingAssembler implements SimpleRepresentationModelAssembler<VettingResponse> {
    @Override
    public void addLinks(EntityModel<VettingResponse> resource) {
        var id = resource.getContent().getId();

        Link updateLink = linkTo(methodOn(VettingRestController.class).update(null, id))
                .withRel("update")
                .withType("PUT");

        var deleteLink = linkTo(methodOn(VettingRestController.class).delete(id))
                .withRel("delete")
                .withType("DELETE");

        resource.add(updateLink, deleteLink);
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<VettingResponse>> resources) {
        var selfLink = linkTo(methodOn(VettingRestController.class).findAll(null))
                .withSelfRel()
                .withType("GET");

        Link createLink = linkTo(methodOn(VettingRestController.class).create(null))
                .withRel("create")
                .withType("POST");

        resources.add(selfLink, createLink);
    }
}
