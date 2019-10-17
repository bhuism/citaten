package nl.appsource.stream.demo.assembler;

import nl.appsource.stream.demo.controller.CitaatController;
import nl.appsource.stream.demo.model.Citaat;
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

public class CitaatResourceAssemblerTests extends BaseResourceAssemblerTests {

    private final CitaatResourceAssembler assembler = new CitaatResourceAssembler();

    @Test
    public void toModel2ShouldWork() {

        final EntityModel<Citaat> resource = assembler.toModel2(new Citaat(-1L, "Frodo", -2L, -3L), null);

        assertThat(resource.getContent().getId(), is(equalTo(-1L)));
        assertThat(resource.getContent().getName(), is(equalTo("Frodo")));
        assertThat(resource.getContent().getSpreker(), is(equalTo(-2L)));
        assertThat(resource.getContent().getCategorie(), is(equalTo(-3L)));

        assertThat(resource.getLinks(), iterableWithSize(2));

        final Link self = linkTo(methodOn(CitaatController.class, -1L).getById(-1L)).withSelfRel().expand();

        assertThat(resource.getLinks(), containsInAnyOrder(self, citaten));

    }

    @Test
    public void toCollectionModelShouldWork() {

        final Mono<CollectionModel<EntityModel<Citaat>>> resource = assembler.toCollectionModel(Flux.fromIterable(Arrays.asList(new Citaat(-1L, "Frodo", -3L, -4L), new Citaat(-2L, "Pippin", -5L, -6L))), null);

        assertThat(resource.block().getLinks().toList(), iterableWithSize(3));
        assertThat(resource.block().getLinks().toList(), containsInAnyOrder(categorien, citaten, sprekers));

    }


}

