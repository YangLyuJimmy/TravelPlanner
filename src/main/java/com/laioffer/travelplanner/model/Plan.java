package com.laioffer.travelplanner.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "plan")
@JsonDeserialize(builder = Plan.Builder.class)
public class Plan implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String note;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // See how a reservation stores stayReservedDates in com.laioffer.staybooking.service.ReservationService
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private List<DailyPlan> dailyPlanList;

    public Plan() {}

    private Plan(Builder builder) {
        this.id = builder.id;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.note = builder.note;
        this.user = builder.user;
        this.dailyPlanList = builder.dailyPlanList;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public User getUser() {
        return user;
    }
    @JsonBackReference
    public void setUser(User user) {
        this.user = user;
    }

    public List<DailyPlan> getDailyPlanList() {
        return dailyPlanList;
    }

    public void setDailyPlanList(List<DailyPlan> dailyPlanList) {
        this.dailyPlanList = dailyPlanList;
    }

    public static class Builder {
        @JsonProperty("id")
        private Long id;
        @JsonProperty("start_date")
        private LocalDate startDate;
        @JsonProperty("end_date")
        private LocalDate endDate;
        @JsonProperty("note")
        private String note;
        @JsonProperty("user")
        private User user;

        @JsonProperty("daily_plan_list")
        private List<DailyPlan> dailyPlanList;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder setNote(String note) {
            this.note = note;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setDailyPlanList(List<DailyPlan> dailyPlanList) {
            this.dailyPlanList = dailyPlanList;
            return this;
        }

        public Plan build() {
            return new Plan(this);
        }
    }
}
