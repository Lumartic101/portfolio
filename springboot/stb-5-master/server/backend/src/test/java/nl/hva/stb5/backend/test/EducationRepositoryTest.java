package nl.hva.stb5.backend.test;

import nl.hva.stb5.backend.models.Education;
import nl.hva.stb5.backend.models.Faculty;
import nl.hva.stb5.backend.models.User;
import nl.hva.stb5.backend.repositories.EducationRepository;
import nl.hva.stb5.backend.repositories.FacultyRepository;
import nl.hva.stb5.backend.repositories.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class EducationRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired // repository is automatically injected into the test instance
    private EducationRepository repository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Test
    void testFindingAEducation() {

        Education u = repository.findById(2);
        assertEquals("Fysiotherapie",u.getName());

    }

    @Test
    @DirtiesContext
    void testAddingAEducation() {

        Education u = new Education(0 , "Testing Education", facultyRepository.findById(1));

        u = repository.save(u);

        assertNotNull(u.getId());

        u = repository.findById(u.getId());

        assertEquals("Testing Education", u.getName());

    }

    @Test
    @DirtiesContext// indicates that the test is dirty and should therefore be closed and removed from the context cache
    void testRemovingAEducation() {

        repository.deleteById(767);

        assertNull(repository.findById(767));
    }

    @Test
    @DirtiesContext
    void testUpdatingAEducation() {

        Education u = repository.findById(3);

        u.setName("Test education Test Test");

        repository.save(u);

        u = repository.findById(3);

        assertEquals("Test education Test Test", u.getName());

    }

}
