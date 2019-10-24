package nl.appsource.stream.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.repository.CitaatRepository;
import nl.appsource.stream.demo.repository.SprekerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CitaatRouter {

    private static final String CITAAT = "citaat";
    private static final Mono<ServerResponse> NOTFOUND = ServerResponse.notFound().build();
    private static final Mono<ServerResponse> CREATED = ServerResponse.status(HttpStatus.CREATED).build();

    private final CitaatRepository citaatRepository;

    private final SprekerRepository sprekerRepository;

    private final CitaatHandler citaatHandler;

    @Bean
    public RouterFunction<ServerResponse> index() {
        return RouterFunctions
                .route(RequestPredicates.GET("/")
                        .and(accept(MediaType.TEXT_HTML)), (r) ->
                        ok().contentType(MediaType.TEXT_HTML).render("index"));
    }

    @Bean
    public RouterFunction<ServerResponse> citaat() {
        return route(GET("/" + CITAAT + "/{uuid}").and(accept(APPLICATION_JSON)), citaatHandler::getOne)
                .and(route(GET("/" + CITAAT).and(accept(APPLICATION_JSON)), citaatHandler::getAll))
                .and(route(POST("/" + CITAAT).and(accept(APPLICATION_JSON)), citaatHandler::create))
                .and(route(DELETE("/" + CITAAT + "/{uuid}").and(accept(APPLICATION_JSON)), citaatHandler::delete))
                .and(route(GET("/" + CITAAT + "/{uuid}/spreker").and(accept(APPLICATION_JSON)), citaatHandler::getCitaatByIdSpreker))
                .and(route(GET("/" + CITAAT + "/{uuid}/categorie").and(accept(APPLICATION_JSON)), citaatHandler::getCitaatByIdCategorie))
                ;
    }

}
