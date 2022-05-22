package com.laioffer.travelplanner.service;


import com.laioffer.travelplanner.exception.PlanNotExistException;
import com.laioffer.travelplanner.model.Plan;
import com.laioffer.travelplanner.model.User;
import com.laioffer.travelplanner.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlanService {
    private PlanRepository planRepository;

    @Autowired
    public PlanService(PlanRepository planRepository){
        this.planRepository = planRepository;
    }

    public List<Plan> ListByUser(String username){
        return planRepository.findByUser(new User.Builder().setUsername(username).build());
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long planId, String username) throws PlanNotExistException {
        Plan plan = planRepository.findByIdAndUser(planId, new User.Builder().setUsername(username).build());
        if (plan == null) {
            throw new PlanNotExistException("Plan doesn't exist");
        }
        planRepository.deleteById(planId);
    }
}