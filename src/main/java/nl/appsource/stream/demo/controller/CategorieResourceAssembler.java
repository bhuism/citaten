package nl.appsource.stream.demo.controller;

import nl.appsource.stream.demo.model.Categorie;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CategorieResourceAssembler implements SimpleRepresentationModelAssembler<Categorie> {

    private final static CategorieController controller = methodOn(CategorieController.class);

    @Override
    public void addLinks(EntityModel<Categorie> resource) {
        resource.add(linkTo(controller.getById(resource.getContent().getId())).withSelfRel());
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Categorie>> resources) {
        resources.add(linkTo(controller.getAll()).withRel("categorien"));
    }
}
