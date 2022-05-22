package com.laioffer.travelplanner.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinTable(name = "daily_plan_points", joinColumns = {@JoinColumn(name = "daily_plan_id")}, inverseJoinColumns = {@JoinColumn(name = "point_id")})
    List<Point> pointList = new ArrayList<>();

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

    @JsonBackReference
    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Point> getPointList() {
        return pointList;
    }

    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
    }
}
