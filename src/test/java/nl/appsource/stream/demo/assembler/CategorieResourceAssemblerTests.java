package nl.appsource.stream.demo.assembler;

import nl.appsource.stream.demo.controller.CategorieController;
import nl.appsource.stream.demo.controller.CitaatController;
import nl.appsource.stream.demo.model.Categorie;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkRelation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class CategorieResourceAssemblerTests {

    final CategorieResourceAssembler assembler = new CategorieResourceAssembler();

    @Test
    public void toModel2ShouldWork() {

        final EntityModel<Categorie> resource = assembler.toModel2(new Categorie(-1L, "Frodo"), null);

        assertThat(resource.getContent().getId(), is(equalTo(-1L)));
        assertThat(resource.getContent().getName(), is(equalTo("Frodo")));

        assertThat(resource.getLinks(), iterableWithSize(1));

        assertThat(resource.getLinks().iterator().next().getHref(), is(equalTo("/categorien/-1")));
        assertThat(resource.getLinks().iterator().next().getRel(), is(equalTo(LinkRelation.of("self"))));

        final Link link = linkTo(methodOn(CategorieController.class, -1L).getById(-1L)).withSelfRel();

        assertThat(resource.getLinks(), IsIterableContainingInAnyOrder.containsInAnyOrder(link));

    }

    @Test
    public void toCollectionModelShouldWork() {

        final Mono<CollectionModel<EntityModel<Categorie>>> resource = assembler.toCollectionModel(Flux.fromIterable(Arrays.asList(new Categorie(-1L, "Frodo"), new Categorie(-2L, "Pippin"))), null);

        final Link categorien = linkTo(methodOn(CategorieController.class).getAll()).withRel("categorien");
        final Link citaten = linkTo(methodOn(CitaatController.class).getAll()).withRel("citaten");

        assertThat(resource.block().getLinks().toList(), IsIterableContainingInAnyOrder.containsInAnyOrder(categorien, citaten));
        assertThat(resource.block().getLinks().toList(), iterableWithSize(2));

    }


}

