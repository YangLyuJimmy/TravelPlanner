package com.laioffer.travelplanner.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "daily_plan")
public class DailyPlan implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // column plan_id also is a foreign key of table plan
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    private LocalDate date;

    // See how a user stores items in com.laioffer.jupiter.dao.FavoriteDao
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "daily_plan_points", joinColumns = {@JoinColumn(name = "daily_plan_id")}, inverseJoinColumns = {@JoinColumn(name = "point_id")})
    Set<Point> pointSet = new HashSet<>();

    public DailyPlan() {}

    public DailyPlan(Plan plan, LocalDate date) {
        this.plan = plan;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
