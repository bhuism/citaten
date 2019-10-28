package nl.appsource.stream.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Slf4j
@Configuration
public class IndexRouter {

    @Bean
    public RouterFunction<ServerResponse> index() {
        return RouterFunctions
                .route(RequestPredicates.GET("/")
                        .and(accept(MediaType.TEXT_HTML)), (r) ->
                        ok().contentType(MediaType.TEXT_HTML).render("index"));
    }

}
