package com.laioffer.travelplanner.repository;



import com.laioffer.travelplanner.model.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    boolean existsById(Long id);

    List<Point> findByLocation(String location);
}
