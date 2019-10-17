package nl.appsource.stream.demo.assembler;

import nl.appsource.stream.demo.model.Citaat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class CitaatResourceAssembler extends BaseResourceAssembler<Citaat> {

    @Override
    public EntityModel<Citaat> addLinks(final EntityModel<Citaat> resource, final ServerWebExchange exchange) {
        resource.add(linkTo(citaatController.getById(resource.getContent().getId())).withSelfRel().expand());
        resource.add(linkTo(citaatController.getAll(null)).withRel("citaten").expand());
        resource.add(linkTo(citaatController.getCitaatByIdSpreker(resource.getContent().getId())).withRel("spreker").expand());
        resource.add(linkTo(citaatController.getCitaatByIdCategorie(resource.getContent().getId())).withRel("categorie").expand());
//        resource.add(linkTo(categorieController.getById(resource.getContent().getCategorie())).withRel("categorie").expand());
        return resource;
    }



    @Override
    public CollectionModel<EntityModel<Citaat>> addLinks(final CollectionModel<EntityModel<Citaat>> resources, final ServerWebExchange exchange) {

        resources.add(linkTo(citaatController.getAll(null)).withRel("citaten").expand());
        resources.add(linkTo(sprekerController.getAll(null)).withRel("sprekers").expand());
        resources.add(linkTo(categorieController.getAll(null)).withRel("categorien").expand());

        return resources;
    }

}
