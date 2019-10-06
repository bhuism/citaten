package nl.appsource.stream.demo.controller;

import nl.appsource.stream.demo.model.Spreker;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class SprekerResourceAssembler implements SimpleRepresentationModelAssembler<Spreker> {

    private final static SprekerController controller = methodOn(SprekerController.class);

    @Override
    public void addLinks(EntityModel<Spreker> resource) {
        resource.add(linkTo(controller.getById(resource.getContent().getId())).withSelfRel());
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Spreker>> resources) {
        resources.add(linkTo(controller.getAll()).withRel("categorien"));
    }
}
