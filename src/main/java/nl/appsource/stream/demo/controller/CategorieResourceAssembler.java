package nl.appsource.stream.demo.controller;

import nl.appsource.stream.demo.model.Categorie;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CategorieResourceAssembler extends BaseResourceAssembler implements SimpleRepresentationModelAssembler<Categorie> {

    @Override
    public void addLinks(EntityModel<Categorie> resource) {
        resource.add(linkTo(categorieController.getById(resource.getContent().getId())).withSelfRel());
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Categorie>> resources) {
        resources.add(linkTo(categorieController.getAll()).withRel("categorien"));
        resources.add(linkTo(methodOn(CitaatController.class).getAll()).withRel("citaten"));
    }
}
