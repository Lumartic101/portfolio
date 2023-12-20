package nl.hva.stb5.backend.controller;

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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)  // integrate the Spring TestContext Framework into JUnit Jupiter
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // use this env to test with real http-server
public class TestAnswerController {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JWTokenUtils tokenGenerator;

    private HttpEntity request;
    private final static String API_PATH = "/answer";

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
        @DisplayName("Get request with { No } query parameters")
        public void testGettingAnswersWithNoParamsShouldSucceed() {
            // create a URI for the GET request (Arrange)
            URI url = UriComponentsBuilder
                    .fromPath(API_PATH)    // the path for this test
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
            assertEquals(response.getBody().get(0), List.of(3.0, "2022-12-09T23:00:00.000+00:00", "FT", "Onderwijs"));
        }

        @Test
        @DisplayName("Get request with { Pillar } query parameters")
        public void testGettingAnswersWithPillarParameterShouldSucceed() {
            // create a URI in which the query parameters are set for the GET request (Arrange)
            URI url = UriComponentsBuilder
                    .fromPath(API_PATH)    // the path for this test
                    .queryParam("pillar", "Bedrijfsvoering")  // the param for this test
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
            assertEquals(response.getBody().get(0), List.of(1.0, "2022-12-07T23:00:00.000+00:00", "FBSV", "Bedrijfsvoering"));
        }

        @Test
        @DisplayName("Get request with { Faculty } query parameters")
        public void testGettingAnswersWithFacultyParameterShouldSucceed() {
            // create a URI in which the query parameters are set for the GET request (Arrange)
            URI url = UriComponentsBuilder
                    .fromPath(API_PATH)    // the path for this test
                    .queryParam("faculty", "FBSV")  // the param for this test
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
            assertEquals(response.getBody().get(0), List.of(2.0, "2022-12-09T23:00:00.000+00:00", "Johan Cruyff University", "Onderwijs"));
        }

        @Test
        @DisplayName("Get request with { Faculty & Pillar } query parameters")
        public void testGettingAnswersWithPillarAndFacultyParametersShouldSucceed() {
            // create a URI in which the query parameters are set for the GET request (Arrange)
            URI url = UriComponentsBuilder
                    .fromPath(API_PATH)    // the path for this test
                    .queryParam("faculty", "FT")  // the param for this test
                    .queryParam("pillar", "Onderwijs")  // the param for this test
                    .build()
                    .encode()
                    .toUri();

            // make an HTTP GET request, pass in request entity in order to get access to path (Act)
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
            assertEquals(response.getBody().get(0), List.of(3.0, "2022-12-09T23:00:00.000+00:00", "Aviation", "Onderwijs"));
        }
    }

    @Nested
    class StandaloneTests {
        @Test
        @DisplayName("Get request with no JWT token")
        public void testGettingAnswersWithTokenShouldFail() throws AuthorizationException {
            // create a URI in which the query parameters are set for the GET request (Arrange)
            URI url = UriComponentsBuilder
                    .fromPath(API_PATH)    // the path for this test
                    .build()
                    .encode()
                    .toUri();

            // make an HTTP GET request, pass in no request entity in order to get server exception (Act)
            ResponseEntity<String> response = restTemplate.getForEntity(
                    url,
                    String.class    // use String because we expect an error response
            );

            // check if the response is correct (Assert)
            assertEquals(response.getStatusCode(), HttpStatus.UNAUTHORIZED);    // no token was provided so assert 401
        }

        @Test
        @DisplayName("Get request with JWT token with no admin rights")
        public void testGettingAnswersWithNoAdminTokenShouldFail() {
            // create a URI in which the query parameters are set (Arrange)
            URI url = UriComponentsBuilder
                    .fromPath(API_PATH)    // the path for this test
                    .build()
                    .encode()
                    .toUri();

            HttpHeaders headers = new HttpHeaders();                    // instantiate the HTTP Header
            String token = tokenGenerator.encode(1, false);  // generate a token with no admin rights
            headers.set("Authorization", token);                        // add the token to the header
            request = new HttpEntity(headers);                          // build the request and add the headers

            // make an HTTP GET request, pass in request entity without admin access, in order to get server exception (Act)
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    String.class    // use String because we expect an error response
            );

            // check if the response is correct (Assert)
            assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);    // token provided no admin rights so assert 403
        }
    }
}
