package com.laioffer.travelplanner.service;

import com.laioffer.travelplanner.exception.SearchException;
import com.laioffer.travelplanner.model.Category;
import com.laioffer.travelplanner.model.Point;
import com.laioffer.travelplanner.repository.CategoryRepository;
import com.laioffer.travelplanner.repository.PointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SearchService {
    private CategoryRepository categoryRepository;
    private PointRepository pointRepository;

    @Autowired
    public SearchService(CategoryRepository categoryRepository, PointRepository pointRepository) {
        this.categoryRepository = categoryRepository;
        this.pointRepository = pointRepository;
    }

    //this method is to return all the points.
    public List<Point> getAllPoints(String location) throws SearchException {
        List<Point> allPoints = pointRepository.findByLocation(location);

        if (allPoints == null || allPoints.isEmpty()) {
            throw new SearchException("There hasn't stored any points");
        }

        return allPoints;
    }

    //this method is to return all the categories in a list.
    public List<Category> getCategoryList(String location) throws SearchException {
        List<Category> categoryList = categoryRepository.findByLocation(location);

        if (categoryList == null || categoryList.isEmpty()) {
            throw new SearchException("There hasn't stored any categories");
        }

        return categoryList;
    }

    //this method is to return points by category.
    public List<Point> getPointsbyCate(String location, String categoryName) throws SearchException {

        Set<Point> resultSet = null;
        try {
            resultSet = categoryRepository.findByTypeAndLocation(categoryName, location).getPointSet();
        } catch (Exception e) {
            throw new SearchException("No or wrong category name");
        }

        if (resultSet == null || resultSet.isEmpty()) {
            throw new SearchException("No or wrong category name");
        }

        List<Point> resultList = new ArrayList<>(resultSet);

        return resultList;
    }

}
