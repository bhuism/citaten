package nl.appsource.stream.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;

import static org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL;

@Slf4j
@SpringBootApplication
//@SpringBootApplication(exclude = HypermediaAutoConfiguration.class)
//@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
//@EnableHypermediaSupport(type = { HAL, HAL_FORMS }, stacks = WebStack.WEBFLUX)
//@EnableAutoConfiguration(exclude = HypermediaAutoConfiguration.class)
@EnableHypermediaSupport(type = HAL)
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
