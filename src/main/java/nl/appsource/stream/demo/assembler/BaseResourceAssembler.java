package nl.appsource.stream.demo.assembler;

import nl.appsource.stream.demo.controller.CategorieController;
import nl.appsource.stream.demo.controller.CitaatController;
import nl.appsource.stream.demo.controller.SprekerController;
import nl.appsource.stream.demo.model.Citaat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.reactive.SimpleReactiveRepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class BaseResourceAssembler<T> implements SimpleReactiveRepresentationModelAssembler<T> {

    protected final static CitaatController citaatController = methodOn(CitaatController.class);
    protected final static SprekerController sprekerController = methodOn(SprekerController.class);
    protected final static CategorieController categorieController = methodOn(CategorieController.class);

    public EntityModel<T> toModel2(final T entity, final ServerWebExchange exchange) {
        final EntityModel<T> e = new EntityModel<>(entity);
        addLinks(e, exchange);
        return e;
    }

}
