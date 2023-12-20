package nl.hva.stb5.backend.rests;


import nl.hva.stb5.backend.models.Pillar;
import nl.hva.stb5.backend.models.Questionnaire;
import nl.hva.stb5.backend.models.Status;
import nl.hva.stb5.backend.rest.security.JWTokenUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.net.URISyntaxException;
import java.sql.Timestamp;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TestQuestionnaireResource {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    JWTokenUtils jwtTokenProvider;

    @Test
    void testCreatingQuestionnaireShouldSucceed() throws URISyntaxException {

        //Arrange : create JWT and create questionnaire
        var tokenString = jwtTokenProvider.encode(4, true);
        Questionnaire q = new Questionnaire(999, "DRAFT Dashboard Bedrijfsvoering 0.9", new Timestamp(2021 - 1 - 31), new Timestamp(2021 - 2 - 5), Status.CONCEPT, new Pillar(20, "Bedrijfsvoering"));

        //Act and assert
        webTestClient
                .post().uri("/questionnaire")
                .headers(http -> http.setBearerAuth(tokenString))
                .body(BodyInserters.fromObject(q))
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void testUpdatingQuestionnaireShouldSucceed() throws URISyntaxException {

        //Arrange : create JWT and create questionnaire
        var tokenString = jwtTokenProvider.encode(4, true);
        Questionnaire q = new Questionnaire(732, "Altered name", new Timestamp(2021 - 1 - 31), new Timestamp(2021 - 2 - 5), Status.CONCEPT, new Pillar(20, "Bedrijfsvoering"));

        //Act and assert
        webTestClient
                .put().uri("/questionnaire/732")
                .headers(http -> http.setBearerAuth(tokenString))
                .body(BodyInserters.fromObject(q))
                .exchange()
                .expectStatus().isOk();
    }
}


