package nl.appsource.stream.demo.assembler;

import nl.appsource.stream.demo.controller.CitaatController;
import nl.appsource.stream.demo.model.Categorie;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CategorieResourceAssembler extends BaseResourceAssembler<Categorie> {

    @Override
    public EntityModel<Categorie> addLinks(final EntityModel<Categorie> resource, final ServerWebExchange exchange) {
        resource.add(WebMvcLinkBuilder.linkTo(categorieController.getById(resource.getContent().getId())).withSelfRel());
        return resource;
    }

    @Override
    public CollectionModel<EntityModel<Categorie>> addLinks(final CollectionModel<EntityModel<Categorie>> resources, final ServerWebExchange exchange) {
        resources.add(WebMvcLinkBuilder.linkTo(categorieController.getAll()).withRel("categorien"));
        resources.add(WebMvcLinkBuilder.linkTo(methodOn(CitaatController.class).getAll()).withRel("citaten"));
        return resources;
    }
}
