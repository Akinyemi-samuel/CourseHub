package com.coursehub.repositories;

import com.coursehub.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCategory(String category);

    List<Course> findByNameContaining(String keyword);

    @Query(

            value = "SELECT * FROM course ORDER BY RAND() LIMIT 4",
            nativeQuery = true
    )
    List<Course>  findByCategoryIdLimit(Long id);


}