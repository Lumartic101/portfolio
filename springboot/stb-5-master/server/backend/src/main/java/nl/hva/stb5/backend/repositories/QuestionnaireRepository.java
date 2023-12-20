package nl.hva.stb5.backend.repositories;

import nl.hva.stb5.backend.models.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public interface QuestionnaireRepository extends JpaRepository<Questionnaire,Integer> {

    List<Questionnaire> findAll();

    Questionnaire findById(int id);

    Questionnaire save(Questionnaire questionnaire);


    Questionnaire deleteById(int id);

}
