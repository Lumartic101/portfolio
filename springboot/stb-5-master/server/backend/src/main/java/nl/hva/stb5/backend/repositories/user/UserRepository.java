package nl.hva.stb5.backend.repositories.user;

import nl.hva.stb5.backend.models.User;

import java.util.List;

public interface UserRepository {
    User save(User user);

    User deleteById(int id);

    List<User> findAll();

    User findById(int id);

    User findByEmail(String email);
}
