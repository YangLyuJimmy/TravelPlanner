package com.laioffer.travelplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "point_image")
public class PointImage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String url;

    @ManyToOne
    @JoinColumn(name = "point_id")
    @JsonIgnore
    private Point point;

    public PointImage() {}

    public PointImage(String url, Point point) {
        this.url = url;
        this.point = point;
    }

    public String getUrl() {
        return url;
    }

    public PointImage setUrl(String url) {
        this.url = url;
        return this;
    }

    public Point getPoint() {
        return point;
    }

    public PointImage setPoint(Point point) {
        this.point = point;
        return this;
    }
}
