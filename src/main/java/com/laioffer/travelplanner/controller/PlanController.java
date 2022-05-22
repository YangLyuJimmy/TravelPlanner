package com.laioffer.travelplanner.controller;

import com.laioffer.travelplanner.model.Plan;
import com.laioffer.travelplanner.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class PlanController {
    private PlanService planService;

    @Autowired
    public PlanController(PlanService planService){
        this.planService = planService;
    }

    @GetMapping(value = "/plans/{username}")
    public List<Plan> listPlans(@PathVariable String username){

        return planService.ListByUser(username);
    }

    @DeleteMapping("/plan/{planId}")
    void deletePlan(@PathVariable Long planId, Principal principal) {
        planService.delete(planId, principal.getName());
    }
}
