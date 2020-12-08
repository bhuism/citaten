package nl.appsource.stream.demo.controller;

import lombok.RequiredArgsConstructor;
import nl.appsource.stream.demo.Util;
import nl.appsource.stream.demo.model.AbstractPersistable;
import nl.appsource.stream.demo.repository.AbstractReactiveRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

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

    private final R2dbcEntityTemplate template;

    @FunctionalInterface
    public interface MyHandlerFunction extends HandlerFunction<ServerResponse>, Function<UUID, Mono<? extends AbstractPersistable>> {
        @Override
        default Mono<ServerResponse> handle(ServerRequest serverRequest) {
            return Mono.just(serverRequest)
                .map(r -> r.pathVariable("uuid"))
                .flatMap(Util::safeUuidValueofMono)
                .flatMap(this::apply)
                .flatMap(p -> ok().contentType(APPLICATION_JSON).body(fromValue(p)))
                .switchIfEmpty(NOTFOUND);
        }
    }

    public Mono<ServerResponse> getOne(final ServerRequest serverRequest) {
        return ((MyHandlerFunction) repository::findByUuid).handle(serverRequest);
    }

    public Mono<ServerResponse> getAll(final ServerRequest serverRequest) {

        Query query = Query.empty()
            .limit(Util.getIntegerOrDefault(serverRequest, "limit", 10))
            .offset(Util.getIntegerOrDefault(serverRequest, "offset", 0));

        final Optional<String> osort = serverRequest.queryParam("sort");

        if (osort.isPresent()) {
            query = query.sort(Sort.by(osort.get().startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC, osort.get().startsWith("-") ? osort.get().substring(1) : osort.get()));
        }

        return ok()
            .contentType(APPLICATION_JSON)
            .body(template.select(modelClazz)
                .matching(query)
                .all(), modelClazz);
    }

    public Mono<ServerResponse> post(final ServerRequest serverRequest) {
        return serverRequest.body(toMono(modelClazz))
            .flatMap(repository::save)
            .flatMap(citaat -> status(HttpStatus.CREATED).contentType(APPLICATION_JSON).body(fromValue(citaat)))
            .switchIfEmpty(NOTFOUND);
    }

    public Mono<ServerResponse> delete(final ServerRequest serverRequest) {
        return Mono.just(serverRequest)
            .map(r -> r.pathVariable("uuid"))
            .flatMap(Util::safeUuidValueofMono)
            .flatMap(repository::findByUuid)
            .flatMap(citaat -> ok().contentType(APPLICATION_JSON).build(repository.delete(citaat)))
            .switchIfEmpty(NOTFOUND);
    }


    public Mono<ServerResponse> patch(final ServerRequest serverRequest) {
        return ((MyHandlerFunction) c -> repository.findByUuid(c).flatMap(repository::save)).handle(serverRequest);
    }

    public Mono<ServerResponse> put(final ServerRequest serverRequest) {
        return serverRequest.body(toMono(modelClazz))
            .flatMap(repository::save)
            .flatMap(citaat -> status(HttpStatus.OK).contentType(APPLICATION_JSON).body(fromValue(citaat)))
            .switchIfEmpty(NOTFOUND);
    }

}
