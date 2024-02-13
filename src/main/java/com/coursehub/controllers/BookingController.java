package com.coursehub.controllers;


import com.coursehub.dto.request.BookingDto;
import com.coursehub.dto.request.WishListDto;
import com.coursehub.model.Booking;
import com.coursehub.model.WishList;
import com.coursehub.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/booking")
@CrossOrigin()
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Map<String, String> addProductToWishList(@RequestBody BookingDto bookingDto) throws Exception {
        bookingService.bookCourse(bookingDto);
        return Map.of("response", "Course Added to Wishlist successfully");
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("{userId}")
    public List<Booking> getAllProductByBookingId(@PathVariable(value = "userId") Long userId) {
        return bookingService.getAllCourseByUserBookingId(userId);
    }


    @DeleteMapping("/{userId}/courses/{courseId}")
    public ResponseEntity<?> removeCourseFrombookings(@PathVariable Long userId, @PathVariable Long courseId) {
        bookingService.deleteCourseFromBooking(userId, courseId);
        return ResponseEntity.ok().build();
    }

}
