package nl.appsource.stream.demo;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.repository.CategorieRepository;
import nl.appsource.stream.demo.repository.CitaatRepository;
import nl.appsource.stream.demo.repository.SprekerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Slf4j
@ExtendWith(SpringExtension.class)
public class RepoTest {

    static {
        Hooks.onOperatorDebug();
    }

    @Autowired
    private CitaatRepository citaatRepository;

    @Autowired
    private SprekerRepository sprekerRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    public void setUp() throws IOException, URISyntaxException {

        Arrays.asList("schema.sql", "testdata.sql")
                .forEach(fileName -> {
                    try {
                        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Files.copy(Paths.get(ClassLoader.getSystemResource(fileName).toURI()), baos);
                        databaseClient.execute(baos.toString(StandardCharsets.UTF_8)).fetch().all().log().subscribe();
                    } catch (IOException | URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    @AfterEach
    public void tearDown() throws IOException, URISyntaxException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Files.copy(Paths.get(ClassLoader.getSystemResource("cleanup.sql").toURI()), baos);
        databaseClient.execute(baos.toString(StandardCharsets.UTF_8)).fetch().all().subscribe();
    }

    @EnableR2dbcRepositories
    public static class ConnectionConvifugration extends AbstractR2dbcConfiguration {
        @Bean
        public H2ConnectionFactory connectionFactory() {
            return new H2ConnectionFactory(
                H2ConnectionConfiguration.builder()
                    .url("mem:" + UUID.randomUUID())
                    .property("DB_CLOSE_DELAY", "-1")
                    .property("DB_CLOSE_ON_EXIT", "FALSE")
                    .build()
            );
        }
    }

    @Test
    public void shouldCountCitaten() {
        citaatRepository.count().as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void shouldFindCitaat() {
        citaatRepository.findByUuid(UUID.fromString("730d19b3-181f-4987-96b2-a03299d3f487"))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void shouldfindSprekerBySitaat() {
        citaatRepository.getSprekerByCitaatId(UUID.fromString("730d19b3-181f-4987-96b2-a03299d3f487"))
                .as(StepVerifier::create)
                .expectNextMatches(e -> {
                    assertThat(e.getId(), is(equalTo(3L)));
                    assertThat(e.getName(), is(equalTo("sOnbekend3")));
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void shouldfindCategorieBySitaat() {
        citaatRepository.getCategorieByCitaatId(UUID.fromString("730d19b3-181f-4987-96b2-a03299d3f487"))
                .as(StepVerifier::create)
                .expectNextMatches(e -> {
                    assertThat(e.getId(), is(equalTo(3L)));
                    assertThat(e.getName(), is(equalTo("conbekend3")));
                    return true;
                })
                .verifyComplete();
    }

}
