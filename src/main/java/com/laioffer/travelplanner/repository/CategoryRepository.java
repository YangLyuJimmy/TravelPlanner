package com.laioffer.travelplanner.repository;

import com.laioffer.travelplanner.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByType(String Type);
}
