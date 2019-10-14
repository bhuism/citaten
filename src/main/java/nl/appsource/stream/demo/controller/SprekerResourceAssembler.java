package nl.appsource.stream.demo.controller;

import nl.appsource.stream.demo.model.Spreker;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class SprekerResourceAssembler extends BaseResourceAssembler implements SimpleRepresentationModelAssembler<Spreker> {

    @Override
    public void addLinks(EntityModel<Spreker> resource) {
        resource.add(linkTo(sprekerController.getById(resource.getContent().getId())).withSelfRel());
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Spreker>> resources) {
        resources.add(linkTo(sprekerController.sprekers()).withRel("sprekers"));
        resources.add(linkTo(methodOn(CitaatController.class).getAll()).withRel("citaten"));

    }
}
