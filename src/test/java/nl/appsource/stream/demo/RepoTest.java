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
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.core.DatabaseClient;
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
        load(databaseClient, "allschema2.sql");
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
        databaseClient.sql(baos.toString(StandardCharsets.UTF_8)).fetch().all().subscribe();
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

    private final UUID testUUID = new UUID(0x9320683af4367413L, 0x8ea76b0425d6250eL);

    @Test
    public void shouldFindCitaat() {
        citaatRepository.findById(testUUID)
            .as(StepVerifier::create)
            .expectNextMatches(e -> {
                assertThat(e.getName()).isEqualTo("Test Citaat from the future of time and space3");
                assertThat(e.getId()).isEqualTo(testUUID);
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void shouldfindSprekerBycitaat() {
        citaatRepository.getSprekerByCitaatUuid(testUUID)
            .as(StepVerifier::create)
            .expectNextMatches(e -> {
                assertThat(e.getName()).isEqualTo("sOnbekend3");
                assertThat(e.getId()).isEqualTo(UUID.fromString("05727f9f-725e-e737-592a-92feec26eaea"));
                return true;
            })
            .verifyComplete();
    }

    @Test
    public void shouldfindCategorieByCitaat() {
        citaatRepository.getCategorieByCitaatUuid(testUUID)
            .as(StepVerifier::create)
            .expectNextMatches(e -> {
                assertThat(e.getName()).isEqualTo("conbekend3");
                assertThat(e.getId()).isEqualTo(new UUID(0xfac072ba03fc0ac3L,0x27036c91a987e434L));
                return true;
            })
            .verifyComplete();
    }

}
