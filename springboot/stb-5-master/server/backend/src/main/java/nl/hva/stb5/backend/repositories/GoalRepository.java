package nl.hva.stb5.backend.repositories;

import nl.hva.stb5.backend.models.Goal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Integer> {
    List<Goal> findAll();

    Goal findById(int id);

    Goal save(Goal goal);

    Goal deleteById(int id);

}
