package nl.hva.stb5.backend.rest;

import com.fasterxml.jackson.annotation.JsonView;
import nl.hva.stb5.backend.models.Faculty;
import nl.hva.stb5.backend.models.Subquestion;
import nl.hva.stb5.backend.repositories.FacultyRepository;
import nl.hva.stb5.backend.views.FacultyView;
import nl.hva.stb5.backend.views.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityManager;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/faculty")
public class FacultyController {
    private FacultyRepository facultyRepository;

    @Autowired
    public FacultyController(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty){
        Faculty savedFaculty = facultyRepository.save(faculty);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedFaculty.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @GetMapping(path = "")
    @JsonView({UserView.UserEducationFaculty.class})
    public @ResponseBody
    List<Faculty> getAllFaculty(){
        return facultyRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Faculty getFacultyById(@PathVariable int id){
        return facultyRepository.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable int id){
        Faculty faculty = facultyRepository.deleteById(id);

        return ResponseEntity.ok(faculty);
    }
}
