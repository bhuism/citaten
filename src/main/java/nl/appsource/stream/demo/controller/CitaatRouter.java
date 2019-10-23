package nl.appsource.stream.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.repository.CitaatRepository;
import nl.appsource.stream.demo.repository.SprekerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;
import static reactor.core.publisher.Mono.justOrEmpty;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CitaatRouter {

    private static final String CITAAT = "citaat";
    private static final Mono<ServerResponse> NOTFOUND = ServerResponse.notFound().build();
    private static final Mono<ServerResponse> CREATED = ServerResponse.status(HttpStatus.CREATED).build();

    private final CitaatRepository citaatRepository;

    private final SprekerRepository sprekerRepository;

    private static Long getLongOrDefault(final ServerRequest request, final String name, final Long value) {

        return request.queryParam(name)
                .flatMap(CitaatRouter::safeLongValueofOptional)
                .orElse(value)
                ;
    }

    private static Long safeLongValueOf(final String value) {
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static UUID safeUuidValueOf(final String value) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static Optional<Long> safeLongValueofOptional(final String value) {
        return Optional.ofNullable(safeLongValueOf(value));
    }

    private static Optional<UUID> safeUuidValueofOptional(final String value) {
        return Optional.ofNullable(safeUuidValueOf(value));
    }

    private static Mono<Long> safeLongValueofMono(final String value) {
        return Mono.justOrEmpty(safeLongValueofOptional(value));
    }

    private static Mono<UUID> safeUuidValueofMono(final String value) {
        return Mono.justOrEmpty(safeUuidValueofOptional(value));
    }

    @Bean
    public RouterFunction<ServerResponse> index() {
        return RouterFunctions
                .route(RequestPredicates.GET("/")
                        .and(accept(MediaType.TEXT_HTML)), (r) ->
                        ok().contentType(MediaType.TEXT_HTML).render("index"));
    }

    @Bean
    public RouterFunction<ServerResponse> citaat() {
        return route(
                GET("/" + CITAAT + "/{uuid}").and(accept(APPLICATION_JSON)),
                request -> justOrEmpty(request.pathVariable("uuid"))
                        .flatMap(CitaatRouter::safeUuidValueofMono)
                        .map(citaatRepository::findByUuid)
                        .flatMap(citaat -> ok().contentType(APPLICATION_JSON).body(fromPublisher(citaat, Citaat.class)))
                        .switchIfEmpty(NOTFOUND)
        ).and(route(
                GET("/" + CITAAT).and(accept(APPLICATION_JSON)),
                request -> ok().contentType(APPLICATION_JSON).body(citaatRepository.findAll().limitRequest(getLongOrDefault(request, "limit", 5L)), Citaat.class)
                )
        ).and(route(
                POST("/" + CITAAT).and(accept(APPLICATION_JSON)),
                request -> request.body(toMono(Citaat.class))
                        .map(citaatRepository::save)
                        .flatMap(citaat -> ServerResponse.status(HttpStatus.CREATED)
                                .contentType(APPLICATION_JSON)
                                .body(fromPublisher(citaat, Citaat.class))
                        )
                        .switchIfEmpty(NOTFOUND)
                )
        ).and(route(
                DELETE("/" + CITAAT + "/{uuid}").and(accept(APPLICATION_JSON)),
                request -> justOrEmpty(request.pathVariable("uuid"))
                        .flatMap(CitaatRouter::safeUuidValueofMono)
                        .map(citaatRepository::deleteByUuid)
                        .then(ok().build())
                        .switchIfEmpty(NOTFOUND)
        ));
    }

}
