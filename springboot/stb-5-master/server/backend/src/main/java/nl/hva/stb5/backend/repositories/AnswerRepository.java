package nl.hva.stb5.backend.repositories;

import nl.hva.stb5.backend.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    List<Answer> findAll();

    Answer findById(int id);

    Answer save(Answer answer);

    Answer deleteById(int id);
}
