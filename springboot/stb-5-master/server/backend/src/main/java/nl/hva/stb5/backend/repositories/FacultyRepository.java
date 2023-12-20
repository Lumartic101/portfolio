package nl.hva.stb5.backend.repositories;

import nl.hva.stb5.backend.models.Faculty;
import nl.hva.stb5.backend.models.Subquestion;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    List<Faculty> findAll();

    Faculty findById(int id);

    Faculty save(Faculty faculty);

    Faculty deleteById(int id);
}
