package com.laioffer.travelplanner.service;


import com.laioffer.travelplanner.exception.PlanNotExistException;
import com.laioffer.travelplanner.model.*;
import com.laioffer.travelplanner.repository.DailyPlanRepository;
import com.laioffer.travelplanner.repository.PlanRepository;
import com.laioffer.travelplanner.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlanService {
    private PlanRepository planRepository;
    private DailyPlanRepository dailyPlanRepository;
    private PointRepository pointRepository;

    @Autowired
    public PlanService(PlanRepository planRepository, DailyPlanRepository dailyPlanRepository, PointRepository pointRepository) {
        this.planRepository = planRepository;
        this.dailyPlanRepository = dailyPlanRepository;
        this.pointRepository = pointRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void savePlan(SavePlanRequestBody savePlanRequestBody, String username) {

        Plan plan = new Plan.Builder()
                .setNote(savePlanRequestBody.getNote())
                .setStartDate(savePlanRequestBody.getDailyPlanList().get(0).getDate())
                .setEndDate(savePlanRequestBody.getDailyPlanList().get(savePlanRequestBody.getDailyPlanList().size() - 1).getDate())
                .setUser(new User.Builder().setUsername(username).build())
                .build();

        Plan savedPlan = planRepository.save(plan);

        savePlanRequestBody.getDailyPlanList().forEach(requestDailyPlan -> {
            DailyPlan savingDailyPlan = new DailyPlan(savedPlan, requestDailyPlan.getDate());

            requestDailyPlan.getPointList().forEach(requestPoint -> {
                Point savedPoint = pointRepository.getById(requestPoint.getId());
                savingDailyPlan.getPointList().add(savedPoint);
            });

            dailyPlanRepository.save(savingDailyPlan);
        });

    }

    public List<Plan> listByUser(String username) {
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