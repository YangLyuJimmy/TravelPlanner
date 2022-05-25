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
    public List<Point> getPoints() {
        return searchService.getAllPoints();
    }

    // when frontend call http://localhost:8080/search/categories, we return all the categories in a list
    @GetMapping(value = "/search/categories")
    public List<Category> getCategories() {
        return searchService.getCategoryList();
    }

    // when frontend call http://localhost:8080/search/category/{category_name}, we return points under that category
    @GetMapping(value = "/search/category")
    public List<Point> getPoints(@RequestParam(value = "category_name", required = true) String categoryName) {
        return searchService.getPointsbyCate(categoryName);
    }

  // when frontend call http://localhost:8080/search without parameters, we return all the points
    // when with parameter "category", input category name, we return all points under that category
    // when with parameter "category_list", input "TRUE"), we return the complete category list.
    @GetMapping(value = "/search")
    public void getPoints(@RequestParam(value ="category", required = false) String category,
                          @RequestParam(value = "category_list", required = false) String getCateList,
                          HttpServletResponse response) throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        if(category != null ){
            response.getWriter().print(new ObjectMapper().writeValueAsString(searchService.getPointsbyCate(category)));
        } else if (getCateList != null && getCateList.equals("TRUE")) {
            response.getWriter().print(new ObjectMapper().writeValueAsString(searchService.getCategoryList()));
        } else {
            response.getWriter().print(new ObjectMapper().writeValueAsString(searchService.getAllPoints()));
        }

    }
}

