package nl.hva.stb5.backend.rest;

import com.fasterxml.jackson.annotation.JsonView;
import nl.hva.stb5.backend.models.Answer;
import nl.hva.stb5.backend.repositories.AnswerRepository;
import nl.hva.stb5.backend.rest.exceptions.AuthorizationException;
import nl.hva.stb5.backend.rest.security.JWTokenInfo;
import nl.hva.stb5.backend.views.AnswerView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.net.URI;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/answer")
public class AnswerController {
    private final AnswerRepository answerRepository;
    private final EntityManager entityManager;

    @Autowired
    public AnswerController(AnswerRepository answerRepository, EntityManager entityManager) {
        this.answerRepository = answerRepository;
        this.entityManager = entityManager;
    }

    @GetMapping()
    public List<Object[]> getAnswers(@RequestParam(value = "pillar", required = false) String pillar,
                                     @RequestParam(value = "faculty", required = false) String faculty,
                                     @RequestAttribute(value = JWTokenInfo.KEY) JWTokenInfo tokenInfo) {
        boolean noPillar = pillar == null || Objects.equals(pillar, "all");
        boolean noFaculty = faculty == null || Objects.equals(faculty, "all");

        TypedQuery<Object[]> answerQuery;

        // check if the user has permission to use given mapping
        if(!tokenInfo.isAdmin()) {
            throw new AuthorizationException("only administrators can get answers");
        }

        // no filters have been selected so create a dataset from everything
        if (noPillar && noFaculty) {
            answerQuery = entityManager.createNamedQuery("find_all_answers", Object[].class);
        }
        // only pillar has been selected so create a dataset from data with that pillar
        else if (!noPillar && noFaculty) {
            answerQuery = entityManager.createNamedQuery("find_answers_by_pillar", Object[].class);
            answerQuery.setParameter(1, pillar);
        }
        // only faculty has been selected so create a dataset from data with that faculty
        else if (noPillar && !noFaculty) {
            answerQuery = entityManager.createNamedQuery("find_answers_by_faculty", Object[].class);
            answerQuery.setParameter(1, faculty);
        }
        // both pillar and faculty have been selected so create a dataset from data with that pillar and faculty
        else {
            answerQuery = entityManager.createNamedQuery("find_answers_by_pillar_and_faculty", Object[].class);
            answerQuery.setParameter(1, pillar);
            answerQuery.setParameter(2, faculty);
        }

        return answerQuery.getResultList();
    }

    @JsonView({AnswerView.submission.class})
    @PostMapping
    public ResponseEntity<Answer> createAnswer(@RequestBody Answer answer, @RequestAttribute(value = JWTokenInfo.KEY) JWTokenInfo tokenInfo) {
        answer.setUserId(tokenInfo.getId());
        answer.setSubmitDate(Timestamp.from(Instant.now()));
        Answer savedAnswer;
        try {
            savedAnswer = answerRepository.save(answer);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "a submission for this questionnaire has already been made by this user", e);
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedAnswer.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

}
