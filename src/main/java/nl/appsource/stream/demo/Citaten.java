package nl.appsource.stream.demo;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;

@Slf4j
@SpringBootApplication
@EnableHypermediaSupport(type = HAL)
@Generated
public class Citaten {

    public static void main(String[] args) {
        SpringApplication.run(Citaten.class, args);
    }

}
