package nl.hva.stb5.backend.rest;

import nl.hva.stb5.backend.models.Progress;
import nl.hva.stb5.backend.models.Questionnaire;
import nl.hva.stb5.backend.repositories.ProgressRepository;
import nl.hva.stb5.backend.rest.exceptions.PreConditionFailedException;
import nl.hva.stb5.backend.rest.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/progress")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "https://stb-5-fe-app-staging.herokuapp.com"})
public class ProgressController {
    private ProgressRepository progressRepository;

    @Autowired
    public ProgressController(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Progress> createProgress(@RequestBody Progress progress){
        Progress savedProgress = progressRepository.save(progress);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedProgress.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @GetMapping
    public @ResponseBody Iterable<Progress> getAllProgress(){
        return progressRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Progress getProgress(@PathVariable int id){
        Progress foundProgress = progressRepository.getById(id);
        if (foundProgress == null) throw new ResourceNotFoundException("Could not find progress with id:" + id);

        return progressRepository.findById(id);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Progress> updateById(@RequestBody Progress progress, @PathVariable int id)
            throws PreConditionFailedException {
        Progress savedProgress = progressRepository.save(progress);

        if(savedProgress.getId() != id)
            throw new PreConditionFailedException(toString());

        return ResponseEntity.ok(savedProgress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Progress> deleteProgress(@PathVariable int id){
        Progress progress = progressRepository.deleteById(id);

        return ResponseEntity.ok(progress);
    }
}
