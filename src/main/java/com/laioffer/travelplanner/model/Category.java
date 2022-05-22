package com.laioffer.travelplanner.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category")
@JsonDeserialize(builder = Category.Builder.class)
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String type;

    // Watch out conflict with the pointSet of DailyPlan!
    // See how a user stores items in com.laioffer.jupiter.dao.FavoriteDao
    @ManyToMany(mappedBy = "categorySet")
    private Set<Point> pointSet= new HashSet<>();

    public Category() {}

    private Category(Builder builder) {
        this.type = builder.type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Point> getPointSet() {
        return pointSet;
    }

    @JsonBackReference
    public void setPointSet(Set<Point> pointSet) {
        this.pointSet = pointSet;
    }

    public static class Builder {
        // type matches with a name of a subcategory in the ResponseBody of TravelAdvisor API
        // But whether a category should be created in Builder Pattern hasn't been decided yet
        // so the value in @JsonProperty() is only temporarily "name".
        @JsonProperty("name")
        private String type;

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }
}
