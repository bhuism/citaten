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

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
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

    protected String baseUrl() {
        return "http://localhost:" + port;
    }

    protected String api() {
        return baseUrl() + "/citaten";
    }


    @Test
    public void testGetEmployeeListSuccess() throws URISyntaxException {

        final String URI = api() + "/{id}";
        final RestTemplate restTemplate = new RestTemplate();

        restTemplate.setMessageConverters(getMessageConverters());

        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaTypes.HAL_JSON));
        final HttpEntity<String> entity = new HttpEntity<String>(headers);

        final ResponseEntity<Citaat> response = restTemplate.exchange(URI, HttpMethod.GET, entity, Citaat.class, "1102");
        final Citaat resource = response.getBody();

        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getHeaders().keySet(), hasItem("Content-Type"));
        assertThat(response.getHeaders(), hasEntry("Content-Type", Arrays.asList(MediaTypes.HAL_JSON_VALUE)));

        assertThat(resource, is(not(nullValue())));
        assertThat(resource.getId(), is(1102L));

    }

    private List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> converters =
                new ArrayList<HttpMessageConverter<?>>();
        converters.add(new MappingJackson2HttpMessageConverter());
        return converters;


    }


}