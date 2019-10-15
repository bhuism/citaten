package nl.appsource.stream.demo;

import nl.appsource.stream.demo.model.Citaat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @LocalServerPort
    private Long port;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private ResponseEntity<Citaat> getCitaat(final Long id) {
        final String URI = baseUrl() + "/citaten/{id}";
        final RestTemplate restTemplate = new RestTemplate();

        restTemplate.setMessageConverters(getMessageConverters());

        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaTypes.HAL_JSON));
        final HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(URI, HttpMethod.GET, entity, Citaat.class, id);
    }

    @Test
    public void testGetEmployeeListSuccess() {

        final ResponseEntity<Citaat> response = getCitaat(1102L);
        final Citaat resource = response.getBody();

        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getHeaders().keySet(), hasItem("Content-Type"));
        assertThat(response.getHeaders(), hasEntry("Content-Type", Collections.singletonList(MediaTypes.HAL_JSON_VALUE)));

        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getId(), is(1102L));
    }

    private List<HttpMessageConverter<?>> getMessageConverters() {
        final List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());
        return converters;


    }


}