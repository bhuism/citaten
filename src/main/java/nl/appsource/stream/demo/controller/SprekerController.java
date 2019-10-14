package nl.appsource.stream.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Spreker;
import nl.appsource.stream.demo.repository.SprekerRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sprekers")
public class SprekerController {

    private final SprekerRepository sprekerRepository;

    private SprekerResourceAssembler sprekerResourceAssembler = new SprekerResourceAssembler();

    @GetMapping
    public Mono<CollectionModel<EntityModel<Spreker>>> sprekers() {

        SprekerController controller = methodOn(SprekerController.class);

        return sprekerRepository.findAll()
                .limitRequest(5)
                .collectList()
                .map(sprekerResourceAssembler::toCollectionModel)
//                .map(s -> new EntityModel<>(s))
//                .flatMap(e -> new CollectionModel<>(e, null))
//                .flatMap(resources -> linkTo(controller.sprekers())
//                        .withSelfRel()
//                        .toMono()
//                    .map(selfLink -> new CollectionModel<EntityModel<Spreker>>(resources, selfLink)))
                ;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EntityModel<Spreker>> create(@RequestBody EntityModel<Spreker> Spreker) {
        return sprekerRepository.save(Spreker.getContent())
                .map(sprekerResourceAssembler::toModel);

    }

    @GetMapping("/{id}")
    public Mono<EntityModel<Spreker>> getById(@PathVariable Long id) {

        final Link link3 = linkTo(methodOn(SprekerController.class).sprekers()).withRel("sprekers");



        return sprekerRepository.findById(id)
//                .map(sprekerResourceAssembler::toModel)
                .map(p -> new EntityModel<>(p))

                .map(spreker -> {
                        spreker.add(link3);
//                    WebFluxLinkBuilder.WebFluxBuilder
//                    Link link = entityLinks.linkForItemResource(Citaat.class, spreker.getContent().getId()).withRel("citaat");
//                    final Link link = linkTo(methodOn(SprekerController.class).getAll()).withRel("sprekers");
//                    spreker.add(link);

                    return spreker;
                })
                ;
    }

}