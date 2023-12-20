package nl.hva.stb5.backend.rest.security;

import nl.hva.stb5.backend.rest.exceptions.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JWTokenUtils tokenUtils;

    @Autowired
    private JWTokenUtils tokenGenerator;

    // path prefixes that will be protected by the authentication filter
    private static final Set<String> SECURED_PATHS =
            Set.of("/users", "/answer", "/cluster", "/education", "/faculty", "/goal", "/pillar", "/progress", "/questionnaire", "/subquestions");

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        try {
            // get requested path
            String path = req.getServletPath();

            // pre-flight (OPTIONS) requests and non-secured paths should pass through without being intercepted
            if (HttpMethod.OPTIONS.matches(req.getMethod()) || SECURED_PATHS.stream().noneMatch(path::startsWith)) {
                chain.doFilter(req, res);
                return;
            }

            // get the encoded token string from the authorization request header
            String encodedToken = req.getHeader(HttpHeaders.AUTHORIZATION);

            // if the token is missing or not valid, you send an error response and abort further processing of the request
            if(encodedToken == null) {
                throw new AuthenticationException("authentication problem");
            }

            // remove the bearer from the encoded token string
            encodedToken = encodedToken.replace("Bearer ", "");

            // get a representation of the token for future usage
            JWTokenInfo tokenInfo = tokenUtils.decode(encodedToken,false);

            // pass-on the token info as an attribute for the request
            req.setAttribute(JWTokenInfo.KEY, tokenInfo);
            
            // proceed with the chain
            chain.doFilter(req, res);
        } catch(AuthenticationException e ) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication error");
        }
    }
}
