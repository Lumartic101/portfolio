package nl.hva.stb5.backend.rest;

import nl.hva.stb5.backend.models.Questionnaire;
import nl.hva.stb5.backend.models.Subquestion;
import nl.hva.stb5.backend.repositories.SubquestionRepository;
import nl.hva.stb5.backend.rest.exceptions.PreConditionFailedException;
import nl.hva.stb5.backend.rest.exceptions.ResourceNotFoundException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/subquestions")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "https://stb-5-fe-app-staging.herokuapp.com"})
public class SubquestionController {
    private SubquestionRepository subquestionRepository;

    @Autowired
    public SubquestionController(SubquestionRepository subquestionRepository) {
        this.subquestionRepository = subquestionRepository;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Subquestion> createSubquestion(@RequestBody Subquestion subquestion){
        Subquestion savedSubquestion = subquestionRepository.save(subquestion);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedSubquestion.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @GetMapping
    public @ResponseBody Iterable<Subquestion> getAllSubquestion(){
        return subquestionRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Subquestion> getSubquestionById(@PathVariable int id){
        Subquestion foundSubquestion = subquestionRepository.getById(id);
        if (foundSubquestion == null) throw new ResourceNotFoundException("could not find subquestion with id:" + id);
        return ResponseEntity.ok(foundSubquestion);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Subquestion> updateById(@RequestBody Subquestion subquestion, @PathVariable int id)
            throws PreConditionFailedException {
        Subquestion savedSubquestion = subquestionRepository.save(subquestion);

        if(savedSubquestion.getId() != id)
            throw new PreConditionFailedException(toString());

        return ResponseEntity.ok(savedSubquestion);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Subquestion> deleteSubquestion(@PathVariable int id){
        Subquestion subquestion = subquestionRepository.deleteById(id);

        return ResponseEntity.ok(subquestion);
    }
}
