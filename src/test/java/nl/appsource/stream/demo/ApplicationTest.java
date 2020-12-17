package nl.appsource.stream.demo;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.controller.Router;
import nl.appsource.stream.demo.model.Categorie;
import nl.appsource.stream.demo.model.Citaat;
import nl.appsource.stream.demo.model.Spreker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@ActiveProfiles("citest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    private final UUID testUUID = new UUID(0x9fce15e35a84e4c1L, 0x80cb3f5011a4350fL);

    private final WebTestClient webClient = WebTestClient.bindToServer()
        .filters(exchangeFilterFunctions -> {
            exchangeFilterFunctions.add(logRequest());
            exchangeFilterFunctions.add(logResponse());
        })
        .build();


    @LocalServerPort
    private Long port;

    @Autowired
    private DatabaseClient databaseClient;

    @BeforeEach
    public void setUpEach() throws IOException, URISyntaxException {
        RepoTest.load(databaseClient, "testdata.sql");
    }

    @AfterEach
    public void tearDownEach() throws IOException, URISyntaxException {
        RepoTest.load(databaseClient, "cleanup.sql");
    }

    private String citaatBaseUrl() {
        return "http://localhost:" + port + '/' + Router.CITAAT;
    }

    private String categorieBaseUrl() {
        return "http://localhost:" + port + '/' + Router.CATEGORIEN;
    }

    private String sprekerBaseUrl() {
        return "http://localhost:" + port + '/' + Router.SPREKERS;
    }

    @Test
    public void testGetCitaat() {

        webClient.get().uri(citaatBaseUrl() + "/{uuid}", testUUID.toString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody(Citaat.class)
            .value(c -> assertThat(c.getId(), is(equalTo(testUUID))))
            .value(c -> assertThat(c.getName(), is(equalTo("Test Citaat from the future of time and space1"))))
            .value(c -> assertThat(c.getSpreker(), is(equalTo(new UUID(0x7d1fd954371f9888L, 0x7b4f0258a2044962L)))))
            .value(c -> assertThat(c.getCategorie(), is(equalTo(new UUID(0x94e4c3bd0c32b687L, 0x1a06c0a607813fb3L)))));

    }

    @Test
    public void testDeleteCitaat() {

        // DELETE
        webClient.delete().uri(citaatBaseUrl() + "/{uuid}", testUUID.toString()).exchange().expectStatus().isOk();

        // CHECK NOT EXISTS
        webClient.get().uri(citaatBaseUrl() + "/{uuid}", testUUID.toString()).exchange().expectStatus().isNotFound();

    }

    @Test
    public void testGetCitaatSpreker() {
        webClient.get().uri(citaatBaseUrl() + "/{uuid}/spreker", testUUID.toString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody(Spreker.class)
            .value(c -> assertThat(c.getId(), is(equalTo(new UUID(0x7d1fd954371f9888L, 0x7b4f0258a2044962L)))))
            .value(c -> assertThat(c.getName(), is(equalTo("sOnbekend1"))))
        ;
    }

    @Test
    public void testGetCitaatCategorie() {
        webClient.get().uri(citaatBaseUrl() + "/{uuid}/categorie", testUUID.toString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody(Categorie.class)
            .value(c -> assertThat(c.getId(), is(equalTo(new UUID(0x94e4c3bd0c32b687L, 0x1a06c0a607813fb3L)))))
            .value(c -> assertThat(c.getName(), is(equalTo("conbekend1"))))
        ;
    }

    @Test
    public void testGetCitaatNotFound() {

        final UUID uuid = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");

        webClient.get().uri(citaatBaseUrl() + "/{uuid}", uuid.toString())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound()
            .expectHeader().contentType(APPLICATION_JSON)
        ;
    }

    @Test
    public void testGetCitaten() {
        webClient.get().uri(citaatBaseUrl())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBodyList(Citaat.class)
            .hasSize(3)
        ;
    }

    @Test
    public void testGetSprekers() {
        webClient.get().uri(sprekerBaseUrl())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBodyList(Spreker.class)
            .hasSize(3)
        ;
    }

    @Test
    public void testGetCategorien() {
        webClient.get().uri(categorieBaseUrl())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBodyList(Categorie.class)
            .hasSize(3)
        ;
    }

    @Test
    public void testDeleteUnknownCitaat() {

        final UUID uuid = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");

        webClient.delete().uri(citaatBaseUrl() + "/{uuid}", uuid.toString())
            .exchange()
            .expectStatus().isNotFound()
            .expectHeader().contentType(APPLICATION_JSON)
        ;
    }

    @Test
    public void testPostCitaat() {

        final UUID authorUuid = new UUID(0xa2a2e622c746647fL, 0x3ef069ad63036857L);
        final UUID genreUuid = new UUID(0xcfa98af2d0191f19L, 0x0c86eefd3c3e3356L);

        final Citaat citaat = new Citaat(null, "HiThere", authorUuid, genreUuid);

        // CREATE
        final Citaat newCitaat = webClient.post().uri(citaatBaseUrl())
            .accept(APPLICATION_JSON)
            .bodyValue(citaat)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(APPLICATION_JSON)
            .expectBody(Citaat.class)
            //.value(c -> assertThat(c.getId(), is(equalTo(uuid))))
            .value(c -> assertThat(c.getName(), is(equalTo("HiThere"))))
            .value(c -> assertThat(c.getSpreker(), is(equalTo(authorUuid))))
            .value(c -> assertThat(c.getCategorie(), is(equalTo(genreUuid))))
            .returnResult()
            .getResponseBody();

        // CHECK EXISTS
        webClient.get().uri(citaatBaseUrl() + "/{uuid}", newCitaat.getId().toString())
            .exchange()
            .expectStatus().isOk()
        ;

    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Request: ");
                clientRequest
                    .headers()
                    .forEach((name, values) -> sb.append(name).append("=").append(values));
                log.debug(sb.toString());
            }
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientRequest -> {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Response: ");
                clientRequest
                    .headers()
                    .asHttpHeaders()
                    .forEach((name, values) -> sb.append(name).append("=").append(values));
                log.debug(sb.toString());
            }
            return Mono.just(clientRequest);
        });
    }

}