package nl.appsource.stream.demo;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.repository.CategorieRepository;
import nl.appsource.stream.demo.repository.CitaatRepository;
import nl.appsource.stream.demo.repository.SprekerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


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

    // schema.sql is not automatically loaded

    @BeforeAll
    public static void setUpAll(@Autowired DatabaseClient databaseClient) throws IOException, URISyntaxException {
        RepoTest.load(databaseClient, "schema.sql");
    }

    @BeforeEach
    public void setUp() throws IOException, URISyntaxException {
        load(databaseClient, "testdata.sql");
    }

    @AfterEach
    public void tearDown() throws IOException, URISyntaxException {
        load(databaseClient, "cleanup.sql");
    }

    public static void load(final DatabaseClient databaseClient, final String fileName) throws URISyntaxException, IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Files.copy(Paths.get(ClassLoader.getSystemResource(fileName).toURI()), baos);
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
                .expectNextMatches(e -> {
                    assertThat(e.getId()).isEqualTo(3L);
                    assertThat(e.getName()).isEqualTo("Test Citaat from the future of time and space3");
                    assertThat(e.getUuid()).isEqualTo(UUID.fromString("730d19b3-181f-4987-96b2-a03299d3f487"));
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void shouldfindSprekerBycitaat() {
        citaatRepository.getSprekerByCitaatId(UUID.fromString("730d19b3-181f-4987-96b2-a03299d3f487"))
                .as(StepVerifier::create)
                .expectNextMatches(e -> {
                    assertThat(e.getId()).isEqualTo(3L);
                    assertThat(e.getName()).isEqualTo("sOnbekend3");
                    assertThat(e.getUuid()).isEqualTo(UUID.fromString("19834cdd-5042-4a68-9875-3178d17debca"));
                    return true;
                })
                .verifyComplete();
    }

    @Test
    public void shouldfindCategorieByCitaat() {
        citaatRepository.getCategorieByCitaatId(UUID.fromString("730d19b3-181f-4987-96b2-a03299d3f487"))
                .as(StepVerifier::create)
                .expectNextMatches(e -> {
                    assertThat(e.getId()).isEqualTo(3L);
                    assertThat(e.getName()).isEqualTo("conbekend3");
                    assertThat(e.getUuid()).isEqualTo(UUID.fromString("eabc8778-13d1-4e11-b8b2-96cdb09f8233"));
                    return true;
                })
                .verifyComplete();
    }

}
