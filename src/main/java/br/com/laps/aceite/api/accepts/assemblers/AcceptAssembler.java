package br.com.treinaweb.twjobs.api.accepts.assemblers;//package br.com.treinaweb.twjobs.api.ships.assemblers;

import br.com.treinaweb.twjobs.api.accepts.controllers.AcceptRestController;
import br.com.treinaweb.twjobs.api.accepts.dtos.AcceptResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AcceptAssembler implements SimpleRepresentationModelAssembler<AcceptResponse> {

    @Override
    public void addLinks(EntityModel<AcceptResponse> resource) {
        var id = resource.getContent().getId();

        var selfLink = linkTo(methodOn(AcceptRestController.class).findById(id))
            .withSelfRel()
            .withType("GET");

        Link updateLink = null;
        try {
            updateLink = linkTo(methodOn(AcceptRestController.class).update(null,id, null))
                .withRel("update")
                .withType("PUT");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var deleteLink = linkTo(methodOn(AcceptRestController.class).delete(id))
            .withRel("delete")
            .withType("DELETE");


        resource.add(
                selfLink,
                updateLink, deleteLink);
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<AcceptResponse>> resources) {
        var selfLink = linkTo(methodOn(AcceptRestController.class).findAll(null))
            .withSelfRel()
            .withType("GET");

        Link createLink = null;
        try {
            createLink = linkTo(methodOn(AcceptRestController.class).create(null, null))
                .withRel("create")
                .withType("POST");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        resources.add(selfLink, createLink);
    }

}
