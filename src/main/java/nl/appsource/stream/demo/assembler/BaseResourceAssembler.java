package nl.appsource.stream.demo.assembler;

import nl.appsource.stream.demo.controller.CategorieController;
import nl.appsource.stream.demo.controller.CitaatController;
import nl.appsource.stream.demo.controller.SprekerController;
import org.springframework.data.domain.Persistable;
import org.springframework.hateoas.server.reactive.SimpleReactiveRepresentationModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class BaseResourceAssembler<T extends Persistable<Long>> implements SimpleReactiveRepresentationModelAssembler<T> {

    protected final static CitaatController citaatController = methodOn(CitaatController.class);
    protected final static SprekerController sprekerController = methodOn(SprekerController.class);
    protected final static CategorieController categorieController = methodOn(CategorieController.class);

}
