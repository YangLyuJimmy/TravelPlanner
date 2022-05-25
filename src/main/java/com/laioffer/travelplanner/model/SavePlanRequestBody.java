package com.laioffer.travelplanner.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SavePlanRequestBody {

    /**
     * note
     */
    private String note;

    /**
     * daily_plans
     */
    @NotEmpty
    @JsonProperty("daily_plans")
    private List<DailyPlan> dailyPlanList;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class DailyPlan {
        /**
         * everyday date
         */
        @NotNull
        private LocalDate date;

        /**
         *  list of points
         */
        @JsonProperty("points")
        private List<Point> pointList;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Point {
        /**
         *  point id
         */
        @NotNull
        private Long id;
    }

}
