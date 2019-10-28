package nl.appsource.stream.demo.controller;

import lombok.RequiredArgsConstructor;
import nl.appsource.stream.demo.Util;
import nl.appsource.stream.demo.model.AbstractPersistable;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.repository.AbstractReactiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@RequiredArgsConstructor
public class AbstractHandler<T extends AbstractPersistable> {

    private static final Mono<ServerResponse> NOTFOUND = status(NOT_FOUND).contentType(APPLICATION_JSON).build();

    private final AbstractReactiveRepository<T> repository;

    private final Class<T> modelClazz;

    public Mono<ServerResponse> getOne(final ServerRequest request) {
        return Mono.just(request)
                .map(r -> r.pathVariable("uuid"))
                .flatMap(Util::safeUuidValueofMono)
                .flatMap(repository::findByUuid)
                .flatMap(p -> ok().contentType(APPLICATION_JSON).body(fromValue(p)))
                .switchIfEmpty(NOTFOUND);
    }

    public Mono<ServerResponse> getAll(final ServerRequest request) {
        return ok().contentType(APPLICATION_JSON).body(repository.findAll().limitRequest(Util.getLongOrDefault(request, "limit", 5L)), modelClazz);
    }

    public Mono<ServerResponse> post(final ServerRequest request) {
        return request.body(toMono(modelClazz))
                .flatMap(repository::save)
                .flatMap(citaat -> status(HttpStatus.CREATED).contentType(APPLICATION_JSON).body(fromValue(citaat)))
                .switchIfEmpty(NOTFOUND);
    }

    public Mono<ServerResponse> delete(final ServerRequest request) {
        return Mono.just(request)
                .map(r -> r.pathVariable("uuid"))
                .flatMap(Util::safeUuidValueofMono)
                .flatMap(repository::findByUuid)
                .flatMap(citaat -> ok().contentType(APPLICATION_JSON).build(repository.delete(citaat)))
                .switchIfEmpty(NOTFOUND);
    }


    public Mono<ServerResponse> patch(final ServerRequest request) {
        return Mono.just(request)
                .map(r -> r.pathVariable("uuid"))
                .flatMap(Util::safeUuidValueofMono)
                .flatMap(repository::findByUuid)
                .flatMap(repository::save)
                .flatMap(p -> ok().contentType(APPLICATION_JSON).body(fromValue(p)))
                .switchIfEmpty(NOTFOUND);
    }

    public Mono<ServerResponse> put(final ServerRequest request) {
        return request.body(toMono(modelClazz))
                .flatMap(repository::save)
                .flatMap(citaat -> status(HttpStatus.OK).contentType(APPLICATION_JSON).body(fromValue(citaat)))
                .switchIfEmpty(NOTFOUND);
    }

}
