package nl.appsource.stream.demo;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.controller.Router;
import nl.appsource.stream.demo.model.Citaat;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Slf4j
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

    private String baseUrl() {
        return "http://localhost:" + port + '/' + Router.CITAAT;
    }

    @Test
    public void testGetCitaat() {

        webClient.get().uri(baseUrl() + "/{uuid}", testUUID.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody(Citaat.class)
                .value(c -> assertThat(c.getUuid(), is(equalTo(testUUID))))
                .value(c -> assertThat(c.getId(), is(equalTo(1L))))
                .value(c -> assertThat(c.getName(), is(equalTo("Test Citaat from the future of time and space"))))
                .value(c -> assertThat(c.getSpreker(), is(equalTo(50313L))))
                .value(c -> assertThat(c.getCategorie(), is(equalTo(73001L))))
        ;

    }

    @Test
    public void testGetCitaatNotFound() {

        final UUID uuid = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");

        webClient.get().uri(baseUrl() + "/{uuid}", uuid.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(APPLICATION_JSON)
        ;
    }

    @Test
    public void testGetCitaten() {

        webClient.get().uri(baseUrl())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBodyList(Citaat.class)
                .hasSize(5)
        ;

    }

    @Test
    public void testDeleteUnknownCitaat() {

        final UUID uuid = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");

        webClient.delete().uri(baseUrl() + "/" + uuid.toString())
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(APPLICATION_JSON)
        ;
    }

    @Test
    public void testCreateCitaat() {

        final UUID uuid = UUID.fromString("aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee");

        final Citaat citaat = new Citaat(null, uuid, "HiThere", 38291L, 73001L);

        // CREATE
        webClient.post().uri(baseUrl())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(citaat)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody(Citaat.class)
                .value(c -> assertThat(c.getUuid(), is(equalTo(uuid))))
                .value(c -> assertThat(c.getName(), is(equalTo("HiThere"))))
                .value(c -> assertThat(c.getSpreker(), is(equalTo(38291L))))
                .value(c -> assertThat(c.getCategorie(), is(equalTo(73001L))))
        ;

        // CHECK EXISTS
        webClient.get().uri(baseUrl() + "/" + uuid.toString())
                .exchange()
                .expectStatus().isOk()
        ;

        // DELETE
        webClient.delete().uri(baseUrl() + "/" + uuid.toString()).exchange().expectStatus().isOk();

        // CHECK NOT EXISTS
        webClient.get().uri(baseUrl() + "/" + uuid.toString()).exchange().expectStatus().isNotFound();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Request: ");
                //append clientRequest method and url
                clientRequest
                        .headers()
                        .forEach((name, values) -> sb.append(name + "=" + values));
                log.debug(sb.toString());
            }
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientRequest -> {
            if (log.isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Response: ");
                //append clientRequest method and url
                clientRequest
                        .headers()
                        .asHttpHeaders()
                        .forEach((name, values) -> sb.append(name + "=" + values));
                log.debug(sb.toString());
            }
            return Mono.just(clientRequest);
        });
    }

}