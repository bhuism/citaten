package nl.appsource.stream.demo;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Citaat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runners.Parameterized;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @LocalServerPort
    private Long port;

    private final RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    void beforeEach(TestInfo testInfo, RepetitionInfo repetitionInfo) {
        int currentRepetition = repetitionInfo.getCurrentRepetition();
        int totalRepetitions = repetitionInfo.getTotalRepetitions();
        String methodName = testInfo.getTestMethod().get().getName();
        log.info(String.format("About to execute repetition %d of %d for %s", currentRepetition, totalRepetitions, methodName));

        switch (currentRepetition) {
            case 1: baseUrl = "http://localhost:" + port + "/citaten" ; break;
            case 2: baseUrl = "http://localhost:" + port + "/citaat" ; break;
            default: throw new IllegalArgumentException();
        }

    }

    @Getter
    @Accessors(fluent = true)
    private String baseUrl;


    private ResponseEntity<Citaat> getCitaat(final UUID uuid) {
        log.debug("baseUrl()=" + baseUrl());
        return restTemplate.getForEntity(new UriTemplate(baseUrl() + "/{uuid}").expand(uuid), Citaat.class);
    }

    private ResponseEntity<List<Citaat>> getCitaten() {
        return restTemplate.exchange(new RequestEntity<Citaat>(GET, (URI.create(baseUrl()))), new ParameterizedTypeReference<List<Citaat>>() {
        });
    }

    private ResponseEntity<Citaat> createCitaat(final Citaat citaat) {
        return restTemplate.postForEntity(URI.create(baseUrl()), citaat, Citaat.class);
    }

    private void deleteCitaat(final UUID uuid) {
        restTemplate.delete(new UriTemplate(baseUrl() + "/{uuid}").expand(uuid));
    }

    @RepeatedTest(2)
    public void testGetCitaat() {

        final ResponseEntity<Citaat> response = getCitaat(UUID.fromString("930d19b3-181f-4987-96b2-a03299d3f487"));
        final Citaat resource = response.getBody();

        assertThat(response.getStatusCode(), is(equalTo(OK)));
        assertThat(response.getHeaders().keySet(), hasItem("Content-Type"));
        assertThat(response.getHeaders(), hasEntry("Content-Type", Collections.singletonList(APPLICATION_JSON_VALUE)));

        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getUuid(), is(equalTo(UUID.fromString("930d19b3-181f-4987-96b2-a03299d3f487"))));
    }

    @RepeatedTest(2)
    public void testGetCitaten() {
        final ResponseEntity<List<Citaat>> response = getCitaten();
        final Collection<Citaat> citaten = response.getBody();

        assertThat(response.getStatusCode(), is(equalTo(OK)));
        assertThat(response.getHeaders().keySet(), hasItem("Content-Type"));
        assertThat(response.getHeaders(), hasEntry("Content-Type", Collections.singletonList(APPLICATION_JSON_VALUE)));

        assertThat(citaten, is(not(nullValue())));
        assertThat(citaten.size(), is(greaterThan(0)));
        assertThat(citaten, hasSize(5));
    }

    @RepeatedTest(2)
    public void testCreateCitaat() {

        final UUID uuid = UUID.fromString("ef014bf5-92e0-473b-a8c4-03b8e17514fb");

        final Citaat citaat = new Citaat(null, uuid, "HiThere", 38291L, 73001L);

        final ResponseEntity<Citaat> response = createCitaat(citaat);
        final Citaat resource = response.getBody();

        assertThat(response.getStatusCode(), is(equalTo(CREATED)));
        assertThat(response.getHeaders().keySet(), hasItem("Content-Type"));
        assertThat(response.getHeaders(), hasEntry("Content-Type", Collections.singletonList(APPLICATION_JSON_VALUE)));

        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getName(), is(equalTo("HiThere")));

        deleteCitaat(uuid);

    }

}