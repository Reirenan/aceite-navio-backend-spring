package br.com.treinaweb.twjobs.api.bercos.assemblers;



import br.com.treinaweb.twjobs.api.bercos.controllers.BercosRestController;
import br.com.treinaweb.twjobs.api.bercos.dtos.BercoResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BercosAssembler implements SimpleRepresentationModelAssembler<BercoResponse> {

    @Override
    public void addLinks(EntityModel<BercoResponse> resource) {
        var id = resource.getContent().getId();

        var selfLink = linkTo(methodOn(BercosRestController.class).findById(id))
            .withSelfRel()
            .withType("GET");

        var updateLink = linkTo(methodOn(BercosRestController.class).update(null, id))
            .withRel("update")
            .withType("PUT");

        var deleteLink = linkTo(methodOn(BercosRestController.class).delete(id))
            .withRel("delete")
            .withType("DELETE");


        resource.add(selfLink, updateLink, deleteLink);
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<BercoResponse>> resources) {
        var selfLink = linkTo(methodOn(BercosRestController.class).findAll(null))
            .withSelfRel()
            .withType("GET");

        var createLink = linkTo(methodOn(BercosRestController.class).create(null))
            .withRel("create")
            .withType("POST");

        resources.add(selfLink, createLink);
    }

}
