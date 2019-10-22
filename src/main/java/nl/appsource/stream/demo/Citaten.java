package nl.appsource.stream.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.web.reactive.config.EnableWebFlux;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;

@Slf4j
@SpringBootApplication
public class Citaten {

    public static void main(String[] args) {
        SpringApplication.run(Citaten.class, args);
    }

}
