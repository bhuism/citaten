package nl.appsource.stream.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static java.net.URI.create;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.resources;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.permanentRedirect;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class IndexRouter {

    private final ResourceLoader resourceLoader;

    @Bean
    public RouterFunction<ServerResponse> index() {
        return route(GET("/"), r -> permanentRedirect(create("https://citaten.odee.net")).build());
    }

    @Bean
    public RouterFunction<ServerResponse> contract() {
        final Resource contract = resourceLoader.getResource(ResourceUtils.CLASSPATH_URL_PREFIX + "/static/openapi.yml");
        return resources("/openapi.yml", contract);
    }

}
