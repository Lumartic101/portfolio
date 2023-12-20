package nl.hva.stb5.backend.repositories;

import nl.hva.stb5.backend.models.Progress;
import nl.hva.stb5.backend.models.Subquestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubquestionRepository extends JpaRepository<Subquestion, Integer> {
    List<Subquestion> findAll();

    Subquestion findById(int id);

    Subquestion save(Subquestion subquestion);

    Subquestion deleteById(int id);
}
