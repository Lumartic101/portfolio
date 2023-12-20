package nl.hva.stb5.backend.rest;

import nl.hva.stb5.backend.models.Goal;
import nl.hva.stb5.backend.repositories.GoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/goal")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "https://stb-5-fe-app-staging.herokuapp.com"})
public class GoalController {
    private GoalRepository goalRepository;

    @Autowired
    public GoalController(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Goal> createGoal(@RequestBody Goal goal){
        Goal savedGoal = goalRepository.save(goal);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedGoal.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Goal> getAllGoal(){
        return goalRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Goal getGoal(@PathVariable int id){
        return goalRepository.findById(id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Goal> deleteGoal(@PathVariable int id){
        Goal goal = goalRepository.deleteById(id);

        return ResponseEntity.ok(goal);
    }
}
