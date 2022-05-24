package com.laioffer.travelplanner.service;

import com.laioffer.travelplanner.model.Category;
import com.laioffer.travelplanner.model.Point;
import com.laioffer.travelplanner.repository.CategoryRepository;
import com.laioffer.travelplanner.repository.PointRepository;
import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;
    private PointRepository pointRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, PointRepository pointRepository) {
        this.categoryRepository = categoryRepository;
        this.pointRepository = pointRepository;
    }

    //this method is for SearchController. It is to return the category list to search request with parameter "categoryList".
    public List<Category> getCategoryList() {
        return categoryRepository.findAll();
    }

    //this method is for DownloadController, to download all category set for each point
    public void downloadCategory(String data) throws RapidAPIException {

        JSONArray API_dataArray = new JSONArray(data);

        try {
            for (int i = 0; i < 33; i++) {
                if (i != 6 && i != 15 && i != 24) {//skip ads at 6, 15, 24
                    JSONObject dataItem = API_dataArray.getJSONObject(i);
                    //add category set loop through subcategory JSONArray
                    JSONArray CategoryArray = dataItem.getJSONArray("subcategory");
                    Point point = pointRepository.getById(Long.parseLong(dataItem.getString("location_id")));
                    if (CategoryArray != null && CategoryArray.length() != 0) {
                        for (int cateIndex = 0; cateIndex < CategoryArray.length(); cateIndex++) {
                            if (!categoryRepository.existsByType(CategoryArray.getJSONObject(cateIndex).getString("name"))) {//if category does not exist in repository
                                Category.Builder builder1 = new Category.Builder();
                                builder1.setType(CategoryArray.getJSONObject(cateIndex).getString("name"));
                                Category newCate = builder1.build();
                                categoryRepository.saveAndFlush(newCate);
                                point.getCategorySet().add(newCate);
                            } else {//if subcategory already exist in category repository
                                Category existingCate = categoryRepository.getById(CategoryArray.getJSONObject(cateIndex).getString("name"));
                                point.getCategorySet().add(existingCate);
                            }
                            categoryRepository.flush();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RapidAPIException("Failed to parse Category");
        }
    }
}
