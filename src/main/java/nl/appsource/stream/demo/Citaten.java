package nl.appsource.stream.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.r2dbc.core.DatabaseClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@SpringBootApplication
public class Citaten {

    @Autowired
    private DatabaseClient databaseClient;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws IOException, URISyntaxException {
        showTables();
//        loadFile(databaseClient, "allschema.sql");
//        showTables();
//        loadFile(databaseClient, "alldata.sql");
//        countCitaten();
    }

    public void showTables() {
        databaseClient.execute("SHOW TABLES;").fetch()
                .all()
                .log()
                .subscribe();
    }

    public void countCitaten() {
        databaseClient.execute("SELECT COUNT(*) FROM CITAAT").fetch().all().log().subscribe();
    }

    public static void main(String[] args) {
        SpringApplication.run(Citaten.class, args);
    }

    public static void loadFile(final DatabaseClient databaseClient, final String fileName) throws URISyntaxException, IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Files.copy(Paths.get(ClassLoader.getSystemResource(fileName).toURI()), baos);
        loadString(databaseClient, baos.toString(StandardCharsets.UTF_8));
    }

    public static void loadString(final DatabaseClient databaseClient, final String sql) throws URISyntaxException, IOException {
        databaseClient.execute(sql).fetch().all().subscribe();
    }


}
