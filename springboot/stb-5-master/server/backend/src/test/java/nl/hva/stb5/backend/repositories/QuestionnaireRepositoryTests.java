package nl.hva.stb5.backend.repositories;

import nl.hva.stb5.backend.models.Pillar;
import nl.hva.stb5.backend.models.Questionnaire;
import nl.hva.stb5.backend.models.Status;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class QuestionnaireRepositoryTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QuestionnaireRepository repo;

    @Test
    void testFindingAQuestionnaire() {

        //Arrange and act
        Questionnaire q = repo.findById(1);

        //Assert
        assertEquals("DRAFT Dashboard onderwijs 0.9", q.getName());

    }

    @Test
    @DirtiesContext
    void testAddingAQuestionnaire() {

        //Arrange
        Questionnaire q = new Questionnaire(999, "DRAFT Dashboard Bedrijfsvoering 0.9", new Timestamp(2021-1-31), new Timestamp(2021-2-5), Status.CONCEPT, new Pillar(20, "Bedrijfsvoering"));

        q = repo.save(q);

        assertNotNull(q.getId());

        q = repo.findById(q.getId());

        assertEquals("DRAFT Dashboard Bedrijfsvoering 0.9", q.getName());

    }

    @Test
    @DirtiesContext
    void testUpdatingAQuestionnaire() {

        Questionnaire q = repo.findById(1);

        q.setName("DRAFT Dashboard Bedrijfsvoering 1.0");

        repo.save(q);

        q = repo.findById(1);

        assertEquals("DRAFT Dashboard Bedrijfsvoering 1.0", q.getName());

    }

}

