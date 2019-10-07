package nl.appsource.stream.demo;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    @LocalServerPort
    private Long port;

    @Test
    public void testGetEmployeeListSuccess() throws URISyntaxException
    {
        final RestTemplate restTemplate = new RestTemplate();

        final String baseUrl = "http://localhost:" + port + "/";
        final URI uri = new URI(baseUrl);
        final ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

        assertThat(result.getStatusCodeValue(), is(200));
    }

}