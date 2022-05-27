package com.laioffer.travelplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.travelplanner.model.Category;
import com.laioffer.travelplanner.model.Point;
import com.laioffer.travelplanner.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class SearchController {

    private SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService){
        this.searchService = searchService;
    }

    // when frontend call http://localhost:8080/search/points, we return all the points
    @GetMapping(value = "/search/points")
    public List<Point> getPoints(@RequestParam(value = "location", required = true) String location) {
        return searchService.getAllPoints(location);
    }

    // when frontend call http://localhost:8080/search/categories, we return all the categories in a list
    @GetMapping(value = "/search/categories")
    public List<Category> getCategories(@RequestParam(value = "location", required = true) String location) {
        return searchService.getCategoryList(location);
    }

    // when frontend call http://localhost:8080/search/category/{category_name}, we return points under that category
    @GetMapping(value = "/search/category")
    public List<Point> getCategory(@RequestParam(value = "location", required = true) String location,
                                   @RequestParam(value = "category_name", required = true) String categoryName) {
        return searchService.getPointsbyCate(location, categoryName);
    }
}

