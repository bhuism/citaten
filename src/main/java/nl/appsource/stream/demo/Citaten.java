package nl.appsource.stream.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@SpringBootApplication
public class Citaten {

    @Autowired
    private DatabaseClient databaseClient;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws IOException {
        showTables();
        loadFile(databaseClient, "allschema.sql");
        showTables();
        loadFile(databaseClient, "alldata.sql");
        countCitaten();
    }

    public void showTables() {
        databaseClient.execute("SHOW TABLES;").fetch()
            .all()
            .subscribe();
    }

    public void countCitaten() {
        databaseClient.execute("SELECT COUNT(*) FROM CITAAT").fetch().all().log().subscribe();
    }

    public static void main(String[] args) {
        SpringApplication.run(Citaten.class, args);
    }

    public static void loadFile(final DatabaseClient databaseClient, final String fileName) throws IOException {
        log.info("Loading " + fileName);
        final String sql = Files.readString(ResourceUtils.getFile("classpath:" + fileName).toPath());
        loadString(databaseClient, sql);
    }

    public static void loadString(final DatabaseClient databaseClient, final String sql) {
        databaseClient.execute(sql).fetch().all().subscribe();
    }

    @Configuration
    @PropertySource("classpath:git.properties")
    public static class XVersionHeaderFilter {

        @Value("${git.commit.id}")
        private String gitCommitId;

        @Component
        public class AddResponseHeaderWebFilter implements WebFilter {

            @Override
            public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
                exchange.getResponse()
                    .getHeaders()
                    .add("X-Version", gitCommitId);
                return chain.filter(exchange);
            }

        }

    }

}
