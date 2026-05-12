package com.Croquetas.repository;

import com.Croquetas.model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContestRepository extends JpaRepository<Contest, Long> {
    List<Contest> findByFinishedFalse();   // active contests
    List<Contest> findByFinishedTrue();
    @Query("SELECT DISTINCT r.contest FROM Recipe r WHERE r.isPublic = true AND r.contest IS NOT NULL ORDER BY r.contest.name")
    List<Contest> findContestsWithPublicRecipes();
}