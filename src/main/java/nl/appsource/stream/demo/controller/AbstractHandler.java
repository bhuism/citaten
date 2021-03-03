package nl.appsource.stream.demo.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.appsource.stream.demo.Util;
import nl.appsource.stream.demo.model.AbstractPersistable;
import nl.appsource.stream.demo.repository.AbstractReactiveRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import static nl.appsource.stream.demo.controller.AbstractHandler.QUERY_PARAMS.LIMIT;
import static nl.appsource.stream.demo.controller.AbstractHandler.QUERY_PARAMS.OFFSET;
import static nl.appsource.stream.demo.controller.AbstractHandler.QUERY_PARAMS.SORT;
import static org.springframework.data.relational.core.query.Criteria.where;
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
        return ((MyHandlerFunction) repository::findById).handle(serverRequest);
    }

    @Getter
    @RequiredArgsConstructor
    enum QUERY_PARAMS {
        LIMIT((q, v) -> q.limit(Util.safeIntegerValueOfOrDefault(v, 10))),
        OFFSET((q, v) -> q.offset(Util.safeIntegerValueOfOrDefault(v, 0))),
        SORT((q, s) -> s.map(osort -> q.sort(Sort.by(osort.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC, osort.startsWith("-") ? osort.substring(1) : osort))).orElse(q));
        private final BiFunction<Query, Optional<String>, Query> mixIn;

    }

    private static Optional<String> getParam(final ServerRequest serverRequest, final String key) {
        return serverRequest.queryParam(key).flatMap((s) -> StringUtils.hasText(s) ? Optional.of(s) : Optional.empty());
    }

    private static Query addMixin(final ServerRequest serverRequest, final Query q, final QUERY_PARAMS operation) {
        return operation.getMixIn().apply(q, getParam(serverRequest, operation.name().toLowerCase()));
    }

    public Mono<ServerResponse> getAll(final ServerRequest serverRequest) {

        Query query =
            Optional.of(
                getParam(serverRequest, "query")
                    .map(queryString -> queryString.length() < 3 ? null : queryString)
                    .map(queryString -> Query.query(where("name").like('%' + queryString + '%')))
                    .orElse(Query.empty()))
                .map(q -> addMixin(serverRequest, q, LIMIT))
                .map(q -> addMixin(serverRequest, q, OFFSET))
                .map(q -> addMixin(serverRequest, q, SORT))
                .orElseGet(Query::empty);

        return ok()
            .contentType(APPLICATION_JSON)
            .body(
                Mono.just(Map.of(
                    "rows", template.select(modelClazz).matching(query).all().collectList().block(),
                    "count", template.select(modelClazz).matching(query).count().block()
                ))
                , modelClazz);
    }

    public Mono<ServerResponse> post(final ServerRequest serverRequest) {
        return serverRequest.body(toMono(modelClazz))
            .map(e -> {
                e.makeNew();
                return e;
            })
            .flatMap(repository::save)
            .flatMap(citaat -> status(HttpStatus.CREATED).contentType(APPLICATION_JSON).body(fromValue(citaat)))
            .switchIfEmpty(NOTFOUND);
    }

    public Mono<ServerResponse> delete(final ServerRequest serverRequest) {
        return Mono.just(serverRequest)
            .map(r -> r.pathVariable("uuid"))
            .flatMap(Util::safeUuidValueofMono)
            .flatMap(repository::findById)
            .flatMap(citaat -> ok().contentType(APPLICATION_JSON).build(repository.delete(citaat)))
            .switchIfEmpty(NOTFOUND);
    }

    public Mono<ServerResponse> patch(final ServerRequest serverRequest) {
        return ((MyHandlerFunction) c -> repository.findById(c).flatMap(repository::save)).handle(serverRequest);
    }

    public Mono<ServerResponse> put(final ServerRequest serverRequest) {
        return serverRequest.body(toMono(modelClazz))
            .flatMap(repository::save)
            .flatMap(citaat -> status(HttpStatus.OK).contentType(APPLICATION_JSON).body(fromValue(citaat)))
            .switchIfEmpty(NOTFOUND);
    }

}
