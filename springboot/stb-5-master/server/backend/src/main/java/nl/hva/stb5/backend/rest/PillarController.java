package nl.hva.stb5.backend.rest;

import com.fasterxml.jackson.annotation.JsonView;
import nl.hva.stb5.backend.models.Pillar;
import nl.hva.stb5.backend.repositories.PillarRepository;
import nl.hva.stb5.backend.views.PillarView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/pillar")
@CrossOrigin(origins = {"http://localhost:4200", "https://stb-5-fe-app-staging.herokuapp.com"})
public class PillarController {
    private PillarRepository pillarRepository;

    @Autowired
    public PillarController(PillarRepository pillarRepository) {
        this.pillarRepository = pillarRepository;
    }

    @PostMapping(value = "/add")
    @JsonView({PillarView.base.class})
    public ResponseEntity<Pillar> createPillar(@RequestBody Pillar pillar){

        Pillar savedPillar = pillarRepository.save(pillar);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedPillar.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/all")
    @JsonView({PillarView.getView.class})
    public @ResponseBody Iterable<Pillar> findAll(){
        return pillarRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Pillar getPillar(@PathVariable int id){
        return pillarRepository.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Pillar> deleteProgress(@PathVariable int id){
        Pillar pillar = pillarRepository.deleteById(id);

        return ResponseEntity.ok(pillar);
    }
}
