package com.coursehub.repositories;

import com.coursehub.model.Booking;
import com.coursehub.model.Course;
import com.coursehub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByUserIdAndCourseId(Long userId, Course courseId);
}