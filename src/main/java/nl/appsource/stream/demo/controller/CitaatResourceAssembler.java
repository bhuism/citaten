package nl.appsource.stream.demo.controller;

import nl.appsource.stream.demo.model.Citaat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.reactive.SimpleReactiveRepresentationModelAssembler;
import org.springframework.web.server.ServerWebExchange;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CitaatResourceAssembler implements SimpleReactiveRepresentationModelAssembler<Citaat> {

    protected final static CitaatController citaatController = methodOn(CitaatController.class);
    protected final static SprekerController sprekerController = methodOn(SprekerController.class);
    protected final static CategorieController categorieController = methodOn(CategorieController.class);

    public EntityModel<Citaat> toModel2(final Citaat entity, final ServerWebExchange exchange) {
        final EntityModel<Citaat> e = new EntityModel<>(entity);
        addLinks(e, exchange);
        return e;
    }

    @Override
    public EntityModel<Citaat> addLinks(final EntityModel<Citaat> resource, final ServerWebExchange exchange) {
        resource.add(linkTo(methodOn(CitaatController.class).getById(resource.getContent().getId())).withSelfRel());
        resource.add(linkTo(methodOn(CitaatController.class).getAll()).withRel("citaten"));
        return resource;
    }

    @Override
    public CollectionModel<EntityModel<Citaat>> addLinks(final CollectionModel<EntityModel<Citaat>> resources, final ServerWebExchange exchange) {

        resources.add(linkTo(methodOn(CitaatController.class).getAll()).withRel("citaten"));
        resources.add(linkTo(methodOn(SprekerController.class).sprekers()).withRel("sprekers"));
        resources.add(linkTo(methodOn(CategorieController.class).getAll()).withRel("categorien"));

        return resources;
    }

}
