package nl.appsource.stream.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.r2dbc.core.DatabaseClient;

@Slf4j
@SpringBootApplication
public class Citaten {

    @Autowired
    private DatabaseClient databaseClient;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() {
        databaseClient.execute("SHOW TABLES;").fetch()
                .all()
                .log()
                .subscribe();

    }

    public static void main(String[] args) {
        SpringApplication.run(Citaten.class, args);
    }

}
