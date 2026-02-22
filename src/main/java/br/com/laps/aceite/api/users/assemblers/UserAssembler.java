package br.com.laps.aceite.api.users.assemblers;//package br.com.treinaweb.twjobs.api.ships.assemblers;

import br.com.laps.aceite.api.users.controllers.UsersCRUDRestController;
import br.com.laps.aceite.api.users.dtos.UserResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler implements SimpleRepresentationModelAssembler<UserResponse> {

    @Override
    public void addLinks(EntityModel<UserResponse> resource) {
        var id = resource.getContent().getId();

//        var selfLink = linkTo(methodOn(UsersCRUDRestController.class).findById(id))
//                .withSelfRel()
//                .withType("GET");

        Link updateLink = null;
        updateLink = linkTo(methodOn(UsersCRUDRestController.class).update(id, null))
                .withRel("update")
                .withType("PUT");

        var deleteLink = linkTo(methodOn(UsersCRUDRestController.class).delete(id))
                .withRel("delete")
                .withType("DELETE");

        resource.add(
 //               selfLink,
                updateLink, deleteLink);
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<UserResponse>> resources) {
        var selfLink = linkTo(methodOn(UsersCRUDRestController.class).findAll(null))
                .withSelfRel()
                .withType("GET");

//        Link createLink = null;
//        try {
//            createLink = linkTo(methodOn(UsersCRUDRestController.class).create(null, null))
//                    .withRel("create")
//                    .withType("POST");
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }

        resources.add(selfLink
                //,
        //        createLink
        );
    }
}