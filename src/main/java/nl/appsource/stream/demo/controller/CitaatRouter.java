package nl.appsource.stream.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.repository.CitaatRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.justOrEmpty;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CitaatRouter {

    private static final String CITAAT = "citaat";

    private final CitaatRepository citaatRepository;

    private static final Mono<ServerResponse> NOTFOUND = ServerResponse.notFound().build();

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
                GET("/" + CITAAT + "/{id}").and(accept(APPLICATION_JSON)),
                request -> justOrEmpty(request.pathVariable("id"))
                        .map(Long::valueOf)
                        .map(citaatRepository::findById)
                        .flatMap(citaat -> ok().contentType(APPLICATION_JSON).body(fromPublisher(citaat, Citaat.class)))
                        .switchIfEmpty(NOTFOUND)
        ).and(route(
                GET("/" + CITAAT).and(accept(APPLICATION_JSON)),
                request -> ok().contentType(APPLICATION_JSON).body(citaatRepository.findAll().limitRequest(5), Citaat.class)
                )
        );

    }

}
