package nl.appsource.stream.demo.assembler;

import nl.appsource.stream.demo.controller.CitaatController;
import nl.appsource.stream.demo.model.Spreker;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SprekerResourceAssembler extends BaseResourceAssembler<Spreker> {

    @Override
    public EntityModel<Spreker> addLinks(final EntityModel<Spreker> resource, final ServerWebExchange exchange) {
        resource.add(WebMvcLinkBuilder.linkTo(sprekerController.getById(resource.getContent().getId())).withSelfRel().expand()  );
        return resource;
    }

    @Override
    public CollectionModel<EntityModel<Spreker>> addLinks(final CollectionModel<EntityModel<Spreker>> resources, final ServerWebExchange exchange) {
        resources.add(WebMvcLinkBuilder.linkTo(sprekerController.getAll(null)).withRel("sprekers").expand());
        resources.add(WebMvcLinkBuilder.linkTo(citaatController.getAll(null)).withRel("citaten").expand());
        return resources;
    }

}
