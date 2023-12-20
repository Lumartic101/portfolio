package nl.hva.stb5.backend.controller;

import nl.hva.stb5.backend.models.Education;
import nl.hva.stb5.backend.repositories.FacultyRepository;
import nl.hva.stb5.backend.rest.exceptions.AuthorizationException;
import nl.hva.stb5.backend.rest.security.JWTokenUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestEducationController {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JWTokenUtils tokenGenerator;

    private HttpEntity request;
    private final static String API_PATH = "/education";

    @Nested
    class InitializedTests {
        @BeforeEach
        void setup() {
            HttpHeaders headers = new HttpHeaders();                    // instantiate the HTTP Header
            String token = tokenGenerator.encode(1, true);   // generate a token with admin rights
            headers.set("Authorization", token);                        // add the token to the header
            request = new HttpEntity(headers);                          // build the request and add the headers
        }

        @Test
        @DisplayName("Get request with no query parameters")
        public void testGettingAllEducationsWithNoParamsShouldSucceed() {
            // create a URI for the GET request (Arrange)
            URI url = UriComponentsBuilder
                    .fromPath(API_PATH)
                    .build()
                    .encode()
                    .toUri();

            // make an HTTP GET request (Act)
            ResponseEntity<ArrayList> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    ArrayList.class
            );

            // check if the response is correct and the body has the correct content (Assert)
            assertEquals(response.getStatusCode(), HttpStatus.OK);
            assertEquals(response.getHeaders().getContentType(), MediaType.APPLICATION_JSON);
            assertNotNull(response.getBody());
            assertEquals(response.getBody().get(0).toString(), "{id=2, name=Fysiotherapie}");
        }
    }

    @Nested
    class StandaloneTests {
        @Test
        @DisplayName("Get request with no JWT token")
        public void testGettingEducationWithoutTokenShouldFail() throws AuthorizationException {
            // create a URI in which the query parameters are set for the GET request (Arrange)
            URI url = UriComponentsBuilder
                    .fromPath(API_PATH)
                    .build()
                    .encode()
                    .toUri();

            // make an HTTP GET request, pass in no request entity in order to get server exception (Act)
            ResponseEntity<String> response = restTemplate.getForEntity(
                    url,
                    String.class    // use String because we expect an error response
            );

            // check if the response is correct (Assert)
            assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);
        }
    }
}