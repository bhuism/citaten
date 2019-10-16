package nl.appsource.stream.demo.assembler;

import nl.appsource.stream.demo.controller.CategorieController;
import nl.appsource.stream.demo.controller.CitaatController;
import nl.appsource.stream.demo.controller.SprekerController;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class BaseResourceAssemblerTests {

    protected final Link categorien = linkTo(methodOn(CategorieController.class).getAll(null)).withRel("categorien").expand();
    protected final Link citaten = linkTo(methodOn(CitaatController.class).getAll(null)).withRel("citaten").expand();
    protected final Link sprekers = linkTo(methodOn(SprekerController.class).getAll(null)).withRel("sprekers").expand();

}
