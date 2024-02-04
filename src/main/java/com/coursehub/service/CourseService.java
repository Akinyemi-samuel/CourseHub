package com.coursehub.service;

import com.coursehub.model.Course;
import com.coursehub.repositories.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public List<Course> getCoursesByCategory(String category) {
        return courseRepository.findByCategory(category);
    }

    public List<Course> searchCourses(String keyword) {
        return courseRepository.findByNameContaining(keyword);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
