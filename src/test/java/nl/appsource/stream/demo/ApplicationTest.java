package nl.appsource.stream.demo;

import nl.appsource.stream.demo.model.Citaat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.client.Traverson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Collections;

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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @LocalServerPort
    private Long port;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private HttpEntity<Citaat> createHttpEntity(final Citaat citaat) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaTypes.HAL_JSON));
        final HttpEntity<Citaat> entity = new HttpEntity<>(citaat, headers);
        return entity;
    }

    private ResponseEntity<Citaat> getCitaat(final Long id) {
        final String url = baseUrl() + "/citaten/{id}";
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(url, HttpMethod.GET, createHttpEntity(null), Citaat.class, id);
    }

    private CollectionModel<EntityModel<Citaat>> getCitaten() throws URISyntaxException {

        Traverson traverson = new Traverson(new URI(baseUrl() + "/citaten"), MediaTypes.HAL_JSON);
        Traverson.TraversalBuilder tb = traverson.follow("citaten");
        ParameterizedTypeReference<CollectionModel<EntityModel<Citaat>>> typeRefDevices = new ParameterizedTypeReference<>() {
        };
        final CollectionModel<EntityModel<Citaat>> models = tb.toObject(typeRefDevices);

        return models;
    }

    private ResponseEntity<Citaat> createCitaat(final Citaat citaat) throws URISyntaxException {
        final String url = baseUrl() + "/citaten";
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForEntity(url, createHttpEntity(citaat), Citaat.class);
    }

    @Test
    public void testGetCitaat() {

        final ResponseEntity<Citaat> response = getCitaat(1102L);
        final Citaat resource = response.getBody();

        assertThat(response.getStatusCode(), is(equalTo(OK)));
        assertThat(response.getHeaders().keySet(), hasItem("Content-Type"));
        assertThat(response.getHeaders(), hasEntry("Content-Type", Collections.singletonList(MediaTypes.HAL_JSON_VALUE)));

        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getId(), is(equalTo(1102L)));
    }

    @Test
    public void testGetCitaten() throws URISyntaxException {
        final CollectionModel<EntityModel<Citaat>> response = getCitaten();
        final Collection<EntityModel<Citaat>> resources = response.getContent();
        assertThat(resources.size(), is(greaterThan(0)));
        assertThat(resources, hasSize(5));
    }

    @Test
    public void testCreateCitaat() throws URISyntaxException {
        final Citaat citaat = new Citaat(null, "HiThere", 38291L, 73001L);

        final ResponseEntity<Citaat> response = createCitaat(citaat);
        final Citaat resource = response.getBody();

        assertThat(response.getStatusCode(), is(equalTo(CREATED)));
        assertThat(response.getHeaders().keySet(), hasItem("Content-Type"));
        assertThat(response.getHeaders(), hasEntry("Content-Type", Collections.singletonList(MediaTypes.HAL_JSON_VALUE)));

        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getName(), is(equalTo("HiThere")));


    }

}