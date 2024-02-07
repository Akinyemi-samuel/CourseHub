package com.coursehub.service;

import com.coursehub.dto.request.BookingDto;
import com.coursehub.exception.ApiException;
import com.coursehub.model.Booking;
import com.coursehub.model.Course;
import com.coursehub.repositories.BookingRepository;
import com.coursehub.repositories.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class BookingService {

    private final BookingRepository bookingRepository;
    private final CourseRepository courseRepository;

    public BookingService(BookingRepository bookingRepository, CourseRepository courseRepository) {
        this.bookingRepository = bookingRepository;
        this.courseRepository = courseRepository;
    }




    public void bookCourse(BookingDto bookingDto){

        Course course = courseRepository.findById(bookingDto.getCourse()).orElseThrow( () -> new ApiException("Course not found", HttpStatus.NOT_FOUND));
        log.info("Product received by userId -: " + bookingDto.getUser());

        Booking booking = Booking.builder()
                .course(course)
                .user(bookingDto.getUser())
                .totalCost(bookingDto.getTotalCost())
                .bookingDateTime(LocalDateTime.now())
                .build();

    }
}
