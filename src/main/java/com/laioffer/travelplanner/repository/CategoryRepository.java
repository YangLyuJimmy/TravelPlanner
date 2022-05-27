package com.laioffer.travelplanner.repository;

import com.laioffer.travelplanner.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByTypeAndLocation(String type, String location);

    Category getByTypeAndLocation(String type, String location);

    Category findByTypeAndLocation(String type, String location);

    List<Category> findByLocation(String location);
}
