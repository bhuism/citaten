package nl.appsource.stream.demo.assembler;

import nl.appsource.stream.demo.controller.CategorieController;
import nl.appsource.stream.demo.controller.CitaatController;
import nl.appsource.stream.demo.controller.SprekerController;
import nl.appsource.stream.demo.model.Citaat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CitaatResourceAssembler extends BaseResourceAssembler<Citaat> {

    @Override
    public EntityModel<Citaat> addLinks(final EntityModel<Citaat> resource, final ServerWebExchange exchange) {
        resource.add(WebMvcLinkBuilder.linkTo(methodOn(CitaatController.class).getById(resource.getContent().getId())).withSelfRel());
        resource.add(linkTo(methodOn(CitaatController.class).getAll()).withRel("citaten"));
        return resource;
    }

    @Override
    public CollectionModel<EntityModel<Citaat>> addLinks(final CollectionModel<EntityModel<Citaat>> resources, final ServerWebExchange exchange) {

        resources.add(linkTo(methodOn(CitaatController.class).getAll()).withRel("citaten"));
        resources.add(WebMvcLinkBuilder.linkTo(methodOn(SprekerController.class).getAll()).withRel("sprekers"));
        resources.add(WebMvcLinkBuilder.linkTo(methodOn(CategorieController.class).getAll()).withRel("categorien"));

        return resources;
    }

}
