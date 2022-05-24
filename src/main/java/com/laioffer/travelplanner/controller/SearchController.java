package com.laioffer.travelplanner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laioffer.travelplanner.service.CategoryService;
import com.laioffer.travelplanner.service.PointService;
import com.laioffer.travelplanner.service.RapidAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class SearchController {

    private PointService pointService;
    private CategoryService categoryService;

    @Autowired
    public SearchController(PointService pointService, CategoryService categoryService){
        this.pointService = pointService;
        this.categoryService = categoryService;
    }

    // when frontend call http://localhost:8080/search without parameters, we return all points
    // when with parameter "category", input category name, we return all points under that category
    // when with parameter "categoryList", input anything (as long as it's not null), we return the complete category list.
    @RequestMapping(value="/search", method= RequestMethod.GET)
    public void getPoints(@RequestParam(value ="category", required = false) String category,
                          @RequestParam(value = "categoryList", required = false) String getCateList,
                          HttpServletResponse response)
            throws IOException, ServletException{

        response.setContentType("application/json;charset=UTF-8");

        try{
            if(category !=null ){
                response.getWriter().print(new ObjectMapper().writeValueAsString(pointService.getPointsbyCate(category)));
            } else if(getCateList !=null) {
                response.getWriter().print(new ObjectMapper().writeValueAsString(categoryService.getCategoryList()));
            }else{
                response.getWriter().print(new ObjectMapper().writeValueAsString(pointService.getAllPoints()));
            }
        } catch (RapidAPIException e){
            throw new ServletException(e);
        } catch (URISyntaxException e){
            e.printStackTrace();
        }
    }


}

