package nl.hva.stb5.backend.rest;

import nl.hva.stb5.backend.models.Cluster;
import nl.hva.stb5.backend.models.Pillar;
import nl.hva.stb5.backend.repositories.ClusterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/cluster")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8080", "https://stb-5-fe-app-staging.herokuapp.com"})
public class ClusterController {
    private ClusterRepository clusterRepository;

    @Autowired
    public ClusterController(ClusterRepository clusterRepository) {
        this.clusterRepository = clusterRepository;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Cluster> createCluster(@RequestBody Cluster cluster){
        Cluster savedCluster = clusterRepository.save(cluster);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedCluster.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Cluster> getAllCluster(){
        return clusterRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Cluster getCluster(@PathVariable int id){
        return clusterRepository.findById(id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Cluster> deleteCluster(@PathVariable int id){
        Cluster cluster = clusterRepository.deleteById(id);

        return ResponseEntity.ok(cluster);
    }
}
