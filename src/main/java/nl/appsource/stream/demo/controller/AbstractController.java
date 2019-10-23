package nl.appsource.stream.demo.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.AbstractPersistable;
import nl.appsource.stream.demo.repository.AbstractReactiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @param <T>
 */

@Validated
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractController<T extends AbstractPersistable> {

    private final AbstractReactiveRepository<T> repository;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public Flux<T> getAll(@RequestParam(required = false, defaultValue = "5") @Max(100) @Min(1) final Long limit) {
        log.debug("getAll() limit=" + limit);
        return repository.findAll().limitRequest(limit);
    }

    @GetMapping(value = "/{uuid}", produces = APPLICATION_JSON_VALUE)
    public abstract Mono<T> getByUuid(@PathVariable final UUID uuid);

    @DeleteMapping(value = "/{uuid}")
    public abstract Mono<Void> deleteByUuid(@PathVariable final UUID uuid);

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<T> create(@RequestBody T entity) {
        log.debug("create() entity=" + entity);
        return repository.save(entity);
    }
}
