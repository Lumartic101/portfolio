package nl.hva.stb5.backend.rest;

import com.fasterxml.jackson.annotation.JsonView;
import nl.hva.stb5.backend.models.Questionnaire;
import nl.hva.stb5.backend.repositories.QuestionnaireRepository;
import nl.hva.stb5.backend.rest.exceptions.AuthorizationException;
import nl.hva.stb5.backend.rest.exceptions.PreConditionFailedException;
import nl.hva.stb5.backend.rest.exceptions.ResourceNotFoundException;
import nl.hva.stb5.backend.rest.security.JWTokenInfo;
import nl.hva.stb5.backend.views.QuestionnaireView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.net.URI;

@Controller
@RequestMapping(path ="/questionnaire")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "https://stb-5-fe-app-staging.herokuapp.com"})
public class QuestionnaireController {
    private final QuestionnaireRepository questionnaireRepository;
    private final EntityManager em;

    @Autowired
    public QuestionnaireController(QuestionnaireRepository questionnaireRepository, EntityManager em) {
        this.questionnaireRepository = questionnaireRepository;
        this.em = em;
    }

    @PostMapping(path = "")
    @JsonView({QuestionnaireView.postView.class})
    public ResponseEntity<Questionnaire> createQuestionnaire(@RequestBody Questionnaire questionnaire){

        Questionnaire savedQuestionnaire = questionnaireRepository.save(questionnaire);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedQuestionnaire.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @GetMapping(path = "")
    @JsonView({QuestionnaireView.getView.class})
    public @ResponseBody Iterable<Questionnaire> getAllQuestionnaires(){
        return questionnaireRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    @JsonView({QuestionnaireView.getView.class})
    public ResponseEntity<Questionnaire> getQuestionnaireById(@RequestAttribute(value = JWTokenInfo.KEY) JWTokenInfo token, @PathVariable int id){
        if (!token.isAdmin())
            throw new AuthorizationException("you are not authorized to request this resource!");
        Questionnaire foundQuestionnaire = questionnaireRepository.findById(id);
        if (foundQuestionnaire == null){
            throw new ResourceNotFoundException("could not find questionnaire with id:" + id);
        }
        return ResponseEntity.ok(foundQuestionnaire);
    }

    @PutMapping(path = "/{id}")
    @CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "https://stb-5-fe-app-staging.herokuapp.com"})
    public ResponseEntity<Questionnaire> updateById(@RequestBody Questionnaire questionnaire, @PathVariable int id)
            throws PreConditionFailedException {
        Questionnaire savedQuestionnaire = questionnaireRepository.save(questionnaire);

        if(savedQuestionnaire.getId() != id)
            throw new PreConditionFailedException(toString());

        return ResponseEntity.ok(savedQuestionnaire);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Questionnaire> deleteQuestionnaire(@PathVariable int id, @RequestAttribute(value = JWTokenInfo.KEY) JWTokenInfo token){
        if (!token.isAdmin())
            throw new AuthorizationException("you are not authorized make this post!");
        
        Questionnaire questionnaire = questionnaireRepository.deleteById(id);

        return ResponseEntity.ok(questionnaire);
    }

    @GetMapping(path = "/admin/preview")
    @JsonView({QuestionnaireView.adminPreview.class})
    public @ResponseBody Iterable<Questionnaire> getQuestionnaireOverviewForAdmin(){
        return questionnaireRepository.findAll();
    }

    @GetMapping(path = "/user/preview")
    @JsonView({QuestionnaireView.userPreview.class})
    public @ResponseBody Iterable<Questionnaire> getQuestionnaireOverviewForUser(){
        return this.em.createNamedQuery("get_available_questionnaires_for_user", Questionnaire.class).getResultList();
    }

    @GetMapping(path = "user/{id}")
    @JsonView({QuestionnaireView.userView.class})
    public ResponseEntity<Questionnaire> getQuestionnaireForUser(@PathVariable int id){
        Questionnaire foundQuestionnaire;
        try {
            foundQuestionnaire = this.em.createNamedQuery("get_questionnaire_for_user", Questionnaire.class)
                    .setParameter(1, id)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "requested questionnaire does not exist or is unavailable", e);
        }
        return ResponseEntity.ok(foundQuestionnaire);
    }
}
