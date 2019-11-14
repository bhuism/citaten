package nl.appsource.stream.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Router {

    public static final String CITAAT = "citaten";
    public static final String SPREKERS = "sprekers";
    public static final String CATEGORIEN = "categorien";

    private final CitaatHandler citaatHandler;
    private final SprekerHandler sprekerHandler;
    private final CategorieHandler categorieHandler;

    @Bean
    public RouterFunction<ServerResponse> endpoints() {
        return route(

                GET("/" + CITAAT + "/{uuid}").and(accept(APPLICATION_JSON)), citaatHandler::getOne)
                .and(route(GET("/" + CITAAT).and(accept(APPLICATION_JSON)), citaatHandler::getAll))
                .and(route(POST("/" + CITAAT).and(accept(APPLICATION_JSON)), citaatHandler::post))
                .and(route(DELETE("/" + CITAAT + "/{uuid}").and(accept(APPLICATION_JSON)), citaatHandler::delete))
                .and(route(GET("/" + CITAAT + "/{uuid}/spreker").and(accept(APPLICATION_JSON)), citaatHandler::getCitaatByIdSpreker))
                .and(route(GET("/" + CITAAT + "/{uuid}/categorie").and(accept(APPLICATION_JSON)), citaatHandler::getCitaatByIdCategorie))
                .and(route(PATCH("/" + CITAAT + "/{uuid}").and(accept(APPLICATION_JSON)), citaatHandler::patch))
                .and(route(PUT("/" + CITAAT + "/{uuid}").and(accept(APPLICATION_JSON)), citaatHandler::put))

                .and(route(GET("/" + SPREKERS + "/{uuid}").and(accept(APPLICATION_JSON)), sprekerHandler::getOne))
                .and(route(GET("/" + SPREKERS).and(accept(APPLICATION_JSON)), sprekerHandler::getAll))
                .and(route(POST("/" + SPREKERS).and(accept(APPLICATION_JSON)), sprekerHandler::post))
                .and(route(DELETE("/" + SPREKERS + "/{uuid}").and(accept(APPLICATION_JSON)), sprekerHandler::delete))
                .and(route(PATCH("/" + SPREKERS + "/{uuid}").and(accept(APPLICATION_JSON)), sprekerHandler::patch))
                .and(route(PUT("/" + SPREKERS + "/{uuid}").and(accept(APPLICATION_JSON)), sprekerHandler::put))

                .and(route(GET("/" + CATEGORIEN + "/{uuid}").and(accept(APPLICATION_JSON)), categorieHandler::getOne))
                .and(route(GET("/" + CATEGORIEN).and(accept(APPLICATION_JSON)), categorieHandler::getAll))
                .and(route(POST("/" + CATEGORIEN).and(accept(APPLICATION_JSON)), categorieHandler::post))
                .and(route(DELETE("/" + CATEGORIEN + "/{uuid}").and(accept(APPLICATION_JSON)), categorieHandler::delete))
                .and(route(PATCH("/" + CATEGORIEN + "/{uuid}").and(accept(APPLICATION_JSON)), categorieHandler::patch))
                .and(route(PUT("/" + CATEGORIEN + "/{uuid}").and(accept(APPLICATION_JSON)), categorieHandler::put))

                .filter((request, next) -> {
                    log.info("Request: " + request.uri() + ", headers: " + request.headers().asHttpHeaders());
                    return next.handle(request).map(response -> {
                        log.info("Response: " + response.headers());
                        return response;
                    });
                });
    }

}
