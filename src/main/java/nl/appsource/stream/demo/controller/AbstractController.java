package nl.appsource.stream.demo.controller;


import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.assembler.BaseResourceAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @param <T>
 */

@Validated
@Slf4j
public class AbstractController<T> {

    @Autowired
    private R2dbcRepository<T, Long> repository;

    @Autowired
    private BaseResourceAssembler<T> resourceAssembler;

    @GetMapping(produces = { APPLICATION_JSON_VALUE, HAL_JSON_VALUE } )
    public Mono<CollectionModel<EntityModel<T>>> getAll(@RequestParam(required = false, defaultValue = "5") @Max(100) @Min(1) final Long limit) {
        log.debug("getAll() limit=" + limit);
        return resourceAssembler
                .toCollectionModel(repository.findAll()
                        .limitRequest(limit), null)
                ;
    }

    @GetMapping(value = "/{id}", produces = { APPLICATION_JSON_VALUE, HAL_JSON_VALUE })
    public Mono<EntityModel<T>> getById(@PathVariable Long id) {
        log.debug("getById() id=" + id);
        return repository
                .findById(id)
                .map(c -> resourceAssembler.toModel2(c, null))
                ;
    }


    @PostMapping(produces = { APPLICATION_JSON_VALUE, HAL_JSON_VALUE })
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<EntityModel<T>> create(@RequestBody EntityModel<T> entity) {
        log.debug("create() entity=" + entity);
        return repository.save(entity.getContent())
                .map(c -> resourceAssembler.toModel2(c, null))
                ;
    }


}
