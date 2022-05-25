package com.laioffer.travelplanner.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "point")
@JsonDeserialize(builder = Point.Builder.class)
public class Point implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String name;
    private Float rating;
    private String description;
    private Float longitude;
    private Float latitude;

    @JsonProperty("image")
    private String imageUrl;

    // See how a user stores items in com.laioffer.jupiter.dao.FavoriteDao
    @ManyToMany(mappedBy = "pointList")
    @JsonBackReference
    private Set<DailyPlan> dailyPlanSet= new HashSet<>();

    // See how a user stores items in com.laioffer.jupiter.dao.FavoriteDao
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinTable(name = "category_of_point",
            joinColumns = { @JoinColumn(name = "point_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id",referencedColumnName = "type")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"point_id", "category_id"})})
    Set<Category> categorySet = new HashSet<>();

    public Point() {}

    private Point(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.rating = builder.rating;
        this.description = builder.description;
        this.longitude = builder.longitude;
        this.latitude = builder.latitude;
        this.imageUrl = builder.imageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Set<DailyPlan> getDailyPlanSet() {
        return dailyPlanSet;
    }

    public void setDailyPlanSet(Set<DailyPlan> dailyPlanSet) {
        this.dailyPlanSet = dailyPlanSet;
    }

    public Set<Category> getCategorySet() {
        return categorySet;
    }

    public void setCategorySet(Set<Category> categorySet) {
        this.categorySet = categorySet;
    }

    public static class Builder {
        // Here @JsonProperty() match each field with the name in ResponseBody of TravelAdvisor API!
        @JsonProperty("location_id")
        private Long id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("rating")
        private Float rating;
        @JsonProperty("description")
        private String description;
        @JsonProperty("longitude")
        private Float longitude;
        @JsonProperty("latitude")
        private Float latitude;
        @JsonProperty("image")
        private String imageUrl;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setRating(Float rating) {
            this.rating = rating;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setLongitude(Float longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder setLatitude(Float latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder setImageUrl(String imageUrl){
            this.imageUrl = imageUrl;
            return this;
        }

        public Point build() {
            return new Point(this);
        }
    }
}
