package nl.hva.stb5.backend.repository;

import nl.hva.stb5.backend.models.User;
import nl.hva.stb5.backend.repositories.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class TestUserRepository {

    @Autowired
    private UserRepository repository;

    @Test
    void testFindingAUserById() {
        User foundUser = repository.findById(4);    // find the user by id
        assertEquals("konrad@gmail.com", foundUser.getEmail()); // check if the correct user is being returned
    }

    @Test
    void testFindingAUserByEmail() {
        User foundUser = repository.findByEmail("konrad@gmail.com");    // find the user by email
        assertEquals("konrad@gmail.com", foundUser.getEmail()); // check if the correct user is being returned
    }

    @Test
    // will mark the context for closure after the test method completes
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void testRemovingAUser() {
        repository.deleteById(4);   // delete the user
        assertNull(repository.findById(4)); // check if the user has been deleted
    }

}
