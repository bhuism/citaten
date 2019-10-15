package nl.appsource.stream.demo.controller;


import nl.appsource.stream.demo.assembler.BaseResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

/**
 * @param <T>
 */

public class AbstractController<T> {

    @Autowired
    private R2dbcRepository<T, Long> repository;

    @Autowired
    private BaseResourceAssembler<T> resourceAssembler;

    @GetMapping
    public Mono<CollectionModel<EntityModel<T>>> getAll() {
        return resourceAssembler
                .toCollectionModel(repository.findAll()
                        .limitRequest(5), null)
                ;
    }

    @GetMapping("/{id}")
    public Mono<EntityModel<T>> getById(@PathVariable Long id) {
        return repository
                .findById(id)
                .map(c -> resourceAssembler.toModel2(c, null))
                ;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EntityModel<T>> create(@RequestBody EntityModel<T> entity) {
        return repository.save(entity.getContent())
                .map(c -> resourceAssembler.toModel2(c, null))
                ;
    }


}
