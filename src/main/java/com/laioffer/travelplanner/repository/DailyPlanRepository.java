package com.laioffer.travelplanner.repository;

import com.laioffer.travelplanner.model.DailyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyPlanRepository extends JpaRepository<DailyPlan, Long> {

}