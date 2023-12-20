package nl.hva.stb5.backend.rest.user;

import com.fasterxml.jackson.annotation.JsonView;
import nl.hva.stb5.backend.models.Education;
import nl.hva.stb5.backend.models.User;
import nl.hva.stb5.backend.repositories.user.UserRepository;
import nl.hva.stb5.backend.rest.exceptions.AuthorizationException;
import nl.hva.stb5.backend.rest.exceptions.PreConditionFailedException;
import nl.hva.stb5.backend.rest.exceptions.ResourceNotFoundException;
import nl.hva.stb5.backend.rest.security.JWTokenInfo;
import nl.hva.stb5.backend.rest.security.PasswordEncoder;
import nl.hva.stb5.backend.views.EducationView;
import nl.hva.stb5.backend.views.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityManager;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:4200", "https://stb-5-fe-app-staging.herokuapp.com"})
public class UserController {
    private final UserRepository userRepo;
    public PasswordEncoder encoder;
    private EntityManager em;

    @Autowired
    public UserController(UserRepository userRepo, EntityManager em, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.em = em;
        this.encoder = encoder;
    }

    @GetMapping("/users")
    @JsonView({UserView.UserEducationFaculty.class})
    public List<User> getAllUsers(@RequestAttribute(value = JWTokenInfo.KEY) JWTokenInfo tokenInfo) {
        // check if the user has permission to use given mapping
        if(!tokenInfo.isAdmin()) {
            throw new AuthorizationException("only administrators can have access to users");
        }

        return userRepo.findAll();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id, @RequestAttribute(value = JWTokenInfo.KEY) JWTokenInfo tokenInfo) {
        // check if the user has permission to use given mapping
        if(!tokenInfo.isAdmin()) {
            throw new AuthorizationException("only administrators can have access to users");
        }

        User user = userRepo.findById(id);

        // if the user was not found
        if (user == null) {
            // no user with the given id has been found
            throw new ResourceNotFoundException(String.format("User-Id=%d was not found", id));
        }

        return user;
    }

    @PostMapping(value = "/users")
    @JsonView({UserView.base.class})
    public ResponseEntity<User> createUser(@RequestBody User user, @RequestAttribute(value = JWTokenInfo.KEY) JWTokenInfo tokenInfo) {
        // check if the user has permission to use given mapping
        if(!tokenInfo.isAdmin()) {
            throw new AuthorizationException("only administrators can create users");
        }

        // encode the users password and save the user
        user.setPassword(encoder.encode(user.getPassword()));
        User savedUser = userRepo.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedUser);
    }

    @PutMapping("/users/{id}")
    @JsonView({UserView.base.class})
    public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable int id, @RequestAttribute(value = JWTokenInfo.KEY) JWTokenInfo tokenInfo) {
        // check if the user has permission to use given mapping
        if(!tokenInfo.isAdmin()) {
            throw new AuthorizationException("only administrators can update users");
        }

        if (user.getId() != id) {
            // the id given in params does not match the body
            throw new PreConditionFailedException(String.format("User-Id =%d was not does not match the path parameter=%d",
                    user.getId(), id));
        }

        User userToBeUpdated = userRepo.findByEmail(user.getEmail());

        // if the user was not found
        if (userToBeUpdated == null) {
            throw new ResourceNotFoundException(String.format("User-Id=%d was not found", id));
        }

        // get the user's password, because the passwords are not stored in the front-end hence no password is being
        // passed in the request
        user.setPassword(userToBeUpdated.getPassword());

        userRepo.save(user);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable int id, @RequestAttribute(value = JWTokenInfo.KEY) JWTokenInfo tokenInfo) {
        // check if the user has permission to use given mapping
        if(!tokenInfo.isAdmin()) {
            throw new AuthorizationException("only administrators can remove users");
        }

        User deletedUser = userRepo.deleteById(id);

        // if the user was not found
        if (deletedUser == null) {
            throw new ResourceNotFoundException(String.format("User-Id=%d was not found", id));
        }

        return ResponseEntity.ok(deletedUser);
    }
}
