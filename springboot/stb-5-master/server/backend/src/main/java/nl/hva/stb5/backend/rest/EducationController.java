package nl.hva.stb5.backend.rest;

import com.fasterxml.jackson.annotation.JsonView;
import nl.hva.stb5.backend.models.Education;
import nl.hva.stb5.backend.models.Pillar;
import nl.hva.stb5.backend.repositories.EducationRepository;
import nl.hva.stb5.backend.rest.exceptions.AuthorizationException;
import nl.hva.stb5.backend.rest.security.JWTokenInfo;
import nl.hva.stb5.backend.views.EducationView;
import nl.hva.stb5.backend.views.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/education")
public class EducationController {
    private EducationRepository educationRepository;

    @Autowired
    public EducationController(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Education> createEducation(@RequestBody Education education){
        Education savedEducation = educationRepository.save(education);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEducation.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @GetMapping(path = "")
    @JsonView({EducationView.base.class})
    public @ResponseBody List<Education> getAllEducation(){
        return educationRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public Education getEducationById(@PathVariable int id){
        return educationRepository.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Education> deleteEducation(@PathVariable int id, @RequestAttribute(value = JWTokenInfo.KEY) JWTokenInfo tokenInfo)
    {
        // check if the user has permission to use given mapping
        if(!tokenInfo.isAdmin()) {
            throw new AuthorizationException("only administrators can get answers");
        }

        Education education = educationRepository.deleteById(id);

        return ResponseEntity.ok(education);
    }
}
