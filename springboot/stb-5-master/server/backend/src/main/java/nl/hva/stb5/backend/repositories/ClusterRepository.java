package nl.hva.stb5.backend.repositories;

import nl.hva.stb5.backend.models.Cluster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClusterRepository extends JpaRepository<Cluster, Integer> {
    List<Cluster> findAll();

    Cluster findById(int id);

    Cluster save(Cluster cluster);

    Cluster deleteById(int id);
}
