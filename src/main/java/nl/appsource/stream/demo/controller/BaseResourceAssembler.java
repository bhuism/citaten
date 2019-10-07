package nl.appsource.stream.demo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class BaseResourceAssembler {

    protected final static CitaatController citaatController = methodOn(CitaatController.class);
    protected final static SprekerController sprekerController = methodOn(SprekerController.class);
    protected final static CategorieController categorieController = methodOn(CategorieController.class);

}
