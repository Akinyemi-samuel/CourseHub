package com.coursehub.repositories;

import com.coursehub.model.Booking;
import com.coursehub.model.Course;
import com.coursehub.model.User;
import com.coursehub.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByUserAndCourse(Long userId, Course courseId);

    @Query(
            value = "SELECT w FROM Booking w WHERE w.user = :userId"
    )
    List<Booking> findAllBookingById(Long userId);


    void deleteByUserAndCourse(Long userId, Course courseId);
}