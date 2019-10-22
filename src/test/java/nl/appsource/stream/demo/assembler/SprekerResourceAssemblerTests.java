package nl.appsource.stream.demo.assembler;

import nl.appsource.stream.demo.controller.SprekerController;
import nl.appsource.stream.demo.model.Spreker;
import org.junit.Test;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class SprekerResourceAssemblerTests extends BaseResourceAssemblerTests {

    private final SprekerResourceAssembler assembler = new SprekerResourceAssembler();

    @Test
    public void toModel2ShouldWork() {

        final EntityModel<Spreker> resource = assembler.toModel(new Spreker(-1L, "Frodo"), null).block();

        assertThat(resource.getContent().getId(), is(equalTo(-1L)));
        assertThat(resource.getContent().getName(), is(equalTo("Frodo")));
        assertThat(resource.getLinks(), iterableWithSize(1));

        final Link link = linkTo(methodOn(SprekerController.class, -1L).getById(-1L)).withSelfRel().expand();

        assertThat(resource.getLinks(), containsInAnyOrder(link));

    }

    @Test
    public void toCollectionModelShouldWork() {

        final Mono<CollectionModel<EntityModel<Spreker>>> resource = assembler.toCollectionModel(Flux.fromIterable(Arrays.asList(new Spreker(-1L, "Frodo"), new Spreker(-2L, "Pippin"))), null);

        assertThat(resource.block().getLinks().toList(), iterableWithSize(2));
        assertThat(resource.block().getLinks().toList(), containsInAnyOrder(sprekers, citaten));

    }


}

