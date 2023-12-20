package nl.hva.stb5.backend.repositories;

import nl.hva.stb5.backend.models.Progress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgressRepository extends JpaRepository<Progress, Integer> {
    List<Progress> findAll();

    Progress findById(int id);

    Progress save(Progress progress);

    Progress deleteById(int id);
}
