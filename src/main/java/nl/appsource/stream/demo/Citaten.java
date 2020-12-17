package nl.appsource.stream.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@SpringBootApplication(proxyBeanMethods = false)
public class Citaten {

    @Autowired
    private DatabaseClient databaseClient;

    @Autowired
    private ResourceLoader resourceLoader;

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup(final ApplicationReadyEvent event) throws IOException {
        showTables();
        loadFile(databaseClient, resourceLoader, "allschema2.sql");
        showTables();
        loadFile(databaseClient, resourceLoader, "alldata2.sql");
        countCitaten();
    }

    public void showTables() {
        databaseClient.sql("SHOW TABLES;").fetch()
            .all()
            .subscribe();
    }

    public void countCitaten() {
        databaseClient.sql("SELECT COUNT(*) FROM quote").fetch().all().log().subscribe();
    }

    public static void main(String[] args) {
        SpringApplication.run(Citaten.class, args);
    }

    public static void loadFile(final DatabaseClient databaseClient, final ResourceLoader resourceLoader, final String fileName) throws IOException {
        log.info("Loading: " + fileName);
        final Resource resource = resourceLoader.getResource(ResourceUtils.CLASSPATH_URL_PREFIX + fileName);
        loadString(databaseClient, asString(resource));
    }


    public static void loadString(final DatabaseClient databaseClient, final String sql) {
        databaseClient.sql(sql).fetch().all().subscribe();
    }

    private static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
