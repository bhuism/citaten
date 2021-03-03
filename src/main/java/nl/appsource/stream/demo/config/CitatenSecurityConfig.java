package nl.appsource.stream.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@EnableWebFluxSecurity
public class CitatenSecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        http.csrf().disable();
        http.authorizeExchange(exchanges -> exchanges.anyExchange().permitAll());

        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080", "https://citaten.odee.net", "http://citaten.odee.net", "https://citaten.jvdkolk.nl"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.addAllowedHeader("total-count");
        configuration.addExposedHeader("total-count");
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        http.cors().configurationSource(source);

        return http.build();
    }
}