package br.com.laps.aceite.api.navios.assembler;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VesselAssembler {
    @Override
    public void addLinks(EntityModel<VesselResponse> resource) {
        var id = resource.getContent().getId();

//        var selfLink = linkTo(methodOn(VesselRestController.class).findById(id))
//            .withSelfRel()
//            .withType("GET");

        Link updateLink = null;
        try {
            updateLink = linkTo(methodOn(VesselRestController.class).update(null, id, null))
                    .withRel("update")
                    .withType("PUT");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var deleteLink = linkTo(methodOn(VesselRestController.class).delete(id))
                .withRel("delete")
                .withType("DELETE");


        resource.add(
//                selfLink,
                updateLink, deleteLink);
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<VesselResponse>> resources) {
        var selfLink = linkTo(methodOn(VesselRestController.class).findAll(null))
                .withSelfRel()
                .withType("GET");

        Link createLink = null;
        try {
            createLink = linkTo(methodOn(VesselRestController.class).create(null, null))
                    .withRel("create")
                    .withType("POST");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        resources.add(selfLink, createLink);
    }
}
