package nl.appsource.stream.demo.controller;

import nl.appsource.stream.demo.model.Citaat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CitaatResourceAssembler extends BaseResourceAssembler implements SimpleRepresentationModelAssembler<Citaat> {

    @Override
    public void addLinks(EntityModel<Citaat> resource) {
        resource.add(linkTo(citaatController.getById(resource.getContent().getId())).withSelfRel());
        resource.add(linkTo(categorieController.getById(resource.getContent().getCategorieId())).withRel(LinkRelation.of("categorie")));
        resource.add(linkTo(sprekerController.getById(resource.getContent().getSprekerId())).withRel(LinkRelation.of("spreker")));
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Citaat>> resources) {
        resources.add(linkTo(citaatController.getAll()).withRel("citaten"));
        resources.add(linkTo(sprekerController.getAll()).withRel("sprekers"));
        resources.add(linkTo(categorieController.getAll()).withRel("categorien"));

    }
}
