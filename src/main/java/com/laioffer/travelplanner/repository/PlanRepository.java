package com.laioffer.travelplanner.repository;

import com.laioffer.travelplanner.model.Plan;
import com.laioffer.travelplanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    Plan findByIdAndUser(Long id, User user);

}
