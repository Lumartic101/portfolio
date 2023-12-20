package nl.hva.stb5.backend.rest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import nl.hva.stb5.backend.models.User;
import nl.hva.stb5.backend.repositories.user.UserRepository;
import nl.hva.stb5.backend.rest.exceptions.AuthenticationException;
import nl.hva.stb5.backend.rest.security.JWTokenInfo;
import nl.hva.stb5.backend.rest.security.JWTokenUtils;
import nl.hva.stb5.backend.rest.security.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthController {
    @Autowired
    private JWTokenUtils tokenGenerator;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JWTokenUtils tokenUtils;

    @PostMapping(path = "/authentication")
    public ResponseEntity<User> authenticateUser(@RequestBody ObjectNode credentials) throws AuthenticationException {
        // get the needed values for authentication from the ObjectNode body, use the 'path'
        // which checks if node is present or not, if not then it returns empty string to avoid null errors.
        String email = credentials.path("email").asText();
        String password = credentials.path("password").asText();

        // first check is a user exists with this email
        User userFound = this.userRepo.findByEmail(email);

        // encode the password given in the body of the request
        String encodedPassword = encoder.encode(password);

        // if there is a problem with the provided credentials, throw an exception without giving any clues
        if (userFound == null || !userFound.validateEncodedPassword(encodedPassword)) {
            throw new AuthenticationException("Invalid credentials.. please try again");
        }

        // issue a token for the user valid
        String tokenString = tokenGenerator.encode(userFound.getId(), userFound.isAdmin());

        // add headers to the response, add expose headers to the Authorization header in order for the client to be able
        // to get access to the Authorization header.
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenString);

        // create a response with the created headers, and the user in the body of the response
        return ResponseEntity.accepted()
                .headers(headers)
                .body(userFound);
    }

    @PostMapping(path = "/refresh-token")
    public ResponseEntity refreshToken(HttpServletRequest request) throws AuthenticationException {
        // get the token from the header, this includes 'Bearer'
        String encodedToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        // there is a problem with the encodedToken, so throw a generic exception without giving too much info
        if(encodedToken == null) {
            throw new AuthenticationException("authentication problem");
        }

        // remove the bearer from the initial string
        encodedToken = encodedToken.replace("Bearer ", "");

        // get a representation of the token
        JWTokenInfo tokenInfo = tokenUtils.decode(encodedToken, true);

        // check if the token can be refreshed
        if(!tokenUtils.isRenewable(tokenInfo)) {
            throw new AuthenticationException("Token is not renewable!");
        }

        // refresh the token for the user
        String tokenString = tokenGenerator.encode(tokenInfo.getId(), tokenInfo.isAdmin());

        // add headers to the response, add expose headers to the Authorization header in order for the client to be able
        // to get access to the Authorization header.
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenString);

        // create a response with the created headers, and the user in the body of the response
        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }


}
