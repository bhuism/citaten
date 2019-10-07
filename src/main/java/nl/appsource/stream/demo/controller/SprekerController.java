package nl.appsource.stream.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Spreker;
import nl.appsource.stream.demo.repository.SprekerRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sprekers")
public class SprekerController {

    private final SprekerRepository SprekerRepository;

    private SprekerResourceAssembler sprekerResourceAssembler = new SprekerResourceAssembler();

    @GetMapping
    public Mono<CollectionModel<EntityModel<Spreker>>> getAll() {
        return SprekerRepository.findAll()
                .limitRequest(5)
                .collectList()
                .map(sprekerResourceAssembler::toCollectionModel);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EntityModel<Spreker>> create(@RequestBody EntityModel<Spreker> Spreker) {
        return SprekerRepository.save(Spreker.getContent())
                .map(sprekerResourceAssembler::toModel);

    }

    @GetMapping("/{id}")
    public Mono<EntityModel<Spreker>> getById(@PathVariable Long id) {
        return SprekerRepository.findById(id)
                .map(sprekerResourceAssembler::toModel)
                .map(spreker -> spreker.add(linkTo(methodOn(SprekerController.class).getAll()).withRel("sprekers")))
                ;
    }

}