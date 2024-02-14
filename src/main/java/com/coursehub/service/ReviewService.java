package com.coursehub.service;

import com.coursehub.dto.response.ReviewDto;
import com.coursehub.model.Review;
import com.coursehub.model.User;
import com.coursehub.model.WishList;
import com.coursehub.repositories.CourseRepository;
import com.coursehub.repositories.ReviewRepository;
import com.coursehub.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ReviewService {


    private final ReviewRepository reviewRepository;


    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public List<ReviewDto> getAllReview() {
       List<ReviewDto> reviewDtos = reviewRepository.findAll().stream().map(review -> new ReviewDto(review.getCourse().getCourseId(),
                 review.getUser().getUserId(),
                 review.getComment(),
                 review.getRating()
                 )).collect(Collectors.toList());
       return reviewDtos;
    }


}
