package com.Croquetas.repository;

import com.Croquetas.model.Contest;
import com.Croquetas.model.ContestRegistration;
import com.Croquetas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ContestRegistrationRepository extends JpaRepository<ContestRegistration, Long> {

    // For ordering contestants by assigned number
    List<ContestRegistration> findByContestOrderByAssignedNumberAsc(Contest contest);

    // To check if a chef already registered for a contest
    Optional<ContestRegistration> findByContestAndChef(Contest contest, User chef);

    // To check if a contestant number is already taken
    boolean existsByContestAndAssignedNumber(Contest contest, int assignedNumber);

    // (Optional) Simple list of registrations for a contest
    List<ContestRegistration> findByContest(Contest contest);
    List<ContestRegistration> findByChef(User chef);
}