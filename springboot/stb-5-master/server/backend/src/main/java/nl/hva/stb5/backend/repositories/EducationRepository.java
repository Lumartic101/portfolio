package nl.hva.stb5.backend.repositories;

import nl.hva.stb5.backend.models.Education;
import nl.hva.stb5.backend.models.Questionnaire;
import nl.hva.stb5.backend.models.Status;
import nl.hva.stb5.backend.models.Subquestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Integer> {

    List<Education> findAll();

    Education findById(int id);

    Education save(Education education);

    Education deleteById(int id);

}
