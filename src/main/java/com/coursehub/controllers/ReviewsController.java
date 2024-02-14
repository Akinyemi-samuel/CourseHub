package com.coursehub.controllers;


import com.coursehub.dto.response.ReviewDto;
import com.coursehub.model.Review;
import com.coursehub.model.WishList;
import com.coursehub.service.ReviewService;
import com.coursehub.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@CrossOrigin()
public class ReviewsController {

    private final ReviewService reviewService;

    public ReviewsController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    public List<ReviewDto> getAllProductByUserWishListId() {
        return reviewService.getAllReview();
    }


}
