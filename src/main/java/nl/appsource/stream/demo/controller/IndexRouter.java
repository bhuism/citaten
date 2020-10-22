package nl.appsource.stream.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static java.net.URI.create;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.ServerResponse.permanentRedirect;

@Slf4j
@Configuration
public class IndexRouter {

    @Value("classpath:/static/openapi.yml")
    private Resource contract;

    @Bean
    public RouterFunction<ServerResponse> index() {
        return RouterFunctions
            .route(GET("/"), r -> permanentRedirect(create("https://citaten.odee.net")).build());
    }

    @Bean
    public RouterFunction<ServerResponse> contract() {
        return RouterFunctions.resources((r) -> Mono.just(contract));
    }

}
