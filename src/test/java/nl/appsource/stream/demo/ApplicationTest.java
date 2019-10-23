package nl.appsource.stream.demo;

import lombok.extern.slf4j.Slf4j;
import nl.appsource.stream.demo.model.Citaat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.actuate.endpoint.web.EndpointMediaTypes;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import java.net.URI;
import java.net.URISyntaxException;
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
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @LocalServerPort
    private Long port;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private ResponseEntity<Citaat> getCitaat(final Long id) {
        final String url = baseUrl() + "/citaten/{id}";
        final URI uri = new UriTemplate(url).expand(id);
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(RequestEntity.get(uri).accept(APPLICATION_JSON).build(), Citaat.class);
    }

    private ResponseEntity<List<Citaat>> getCitaten() {
        final String url = baseUrl() + "/citaten";
        final URI uri = new UriTemplate(url).expand();
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(uri, HttpMethod.GET, null, new ParameterizedTypeReference<List<Citaat>>() {
        });
    }

    private ResponseEntity<Citaat> createCitaat(final Citaat citaat) {
        final String url = baseUrl() + "/citaten";
        final URI uri = new UriTemplate(url).expand();
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(uri, RequestEntity.post(uri).accept(APPLICATION_JSON).body(citaat), Citaat.class);
    }

    @Test
    public void testGetCitaat() {

        final ResponseEntity<Citaat> response = getCitaat(1102L);
        final Citaat resource = response.getBody();

        assertThat(response.getStatusCode(), is(equalTo(OK)));
        assertThat(response.getHeaders().keySet(), hasItem("Content-Type"));
        assertThat(response.getHeaders(), hasEntry("Content-Type", Collections.singletonList(APPLICATION_JSON_VALUE)));

        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getId(), is(equalTo(1102L)));
    }

    @Test
    public void testGetCitaten() throws URISyntaxException {
        final ResponseEntity<List<Citaat>> response = getCitaten();
        final Collection<Citaat> citaten = response.getBody();

        assertThat(response.getStatusCode(), is(equalTo(OK)));
        assertThat(response.getHeaders().keySet(), hasItem("Content-Type"));
        assertThat(response.getHeaders(), hasEntry("Content-Type", Collections.singletonList(APPLICATION_JSON_VALUE)));

        assertThat(citaten.size(), is(greaterThan(0)));
        assertThat(citaten, hasSize(5));
    }

    @Test
    public void testCreateCitaat() throws URISyntaxException {
        final Citaat citaat = new Citaat(null, UUID.fromString("ef014bf5-92e0-473b-a8c4-03b8e17514fb"), "HiThere", 38291L, 73001L);

        final ResponseEntity<Citaat> response = createCitaat(citaat);
        final Citaat resource = response.getBody();

        log.debug("Created: " + resource.getId());

        assertThat(response.getStatusCode(), is(equalTo(CREATED)));
        assertThat(response.getHeaders().keySet(), hasItem("Content-Type"));
        assertThat(response.getHeaders(), hasEntry("Content-Type", Collections.singletonList(APPLICATION_JSON_VALUE)));

        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getName(), is(equalTo("HiThere")));

    }

}