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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
@ActiveProfiles("citest")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    private final UUID testUUID = UUID.fromString("930d19b3-181f-4987-96b2-a03299d3f487");

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
                .value(c -> assertThat(c.getId(), is(nullValue())))
                .value(c -> assertThat(c.getName(), is(equalTo("Test Citaat from the future of time and space1"))))
                .value(c -> assertThat(c.getSpreker(), is(equalTo(1L))))
                .value(c -> assertThat(c.getCategorie(), is(equalTo(1L))))
        ;

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
                .value(c -> assertThat(c.getId(), is(equalTo(UUID.fromString("041b38be-718a-4cb8-80b1-f329e915a21d")))))
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
                .value(c -> assertThat(c.getId(), is(equalTo(UUID.fromString("e1f6fa61-cf10-4bc1-a741-4d7145b74cee")))))
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

        final UUID uuid = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");

        final Citaat citaat = new Citaat(uuid, "HiThere", UUID.randomUUID(), UUID.randomUUID());

        // CREATE
        webClient.post().uri(citaatBaseUrl())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(citaat)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody(Citaat.class)
                .value(c -> assertThat(c.getId(), is(equalTo(uuid))))
                .value(c -> assertThat(c.getName(), is(equalTo("HiThere"))))
                .value(c -> assertThat(c.getSpreker(), is(equalTo(3L))))
                .value(c -> assertThat(c.getCategorie(), is(equalTo(2L))))
        ;

        // CHECK EXISTS
        webClient.get().uri(citaatBaseUrl() + "/{uuid}", uuid.toString())
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