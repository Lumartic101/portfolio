package nl.hva.stb5.backend.repositories;

import nl.hva.stb5.backend.models.Pillar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PillarRepository extends JpaRepository<Pillar, Integer> {
    List<Pillar> findAll();

    Pillar findById(int id);

    Pillar save(Pillar pillar);

    Pillar deleteById(int id);

}
