package com.coursehub.service;

import com.coursehub.dto.request.WishListDto;
import com.coursehub.exception.ApiException;
import com.coursehub.model.Course;
import com.coursehub.model.User;
import com.coursehub.model.WishList;
import com.coursehub.repositories.CourseRepository;
import com.coursehub.repositories.UserRepository;
import com.coursehub.repositories.WishListRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class WishlistService {


    private final CourseService courseService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final WishListRepository wishListRepository;


    public WishlistService(CourseService courseService, UserRepository userRepository, CourseRepository courseRepository, WishListRepository wishListRepository) {
        this.courseService = courseService;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.wishListRepository = wishListRepository;
    }

    public List<WishList> getAllCourseByUserWishListId(Long id)  {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        List<WishList> wishLists = wishListRepository.findAllWishListById(user);
        return wishLists;
    }


    @Transactional
    public void deleteCourseFromWishlist(Long userId, Long courseId) {
        // Check if user and course exist
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found"));

        // Delete the course from the user's wishlist
        wishListRepository.deleteByUserIdAndCourseId(user, course);
    }


    public void addCourseToWishListByUser(WishListDto wishListDto) throws Exception {

        try {
            Course course = courseRepository.findById(wishListDto.getCourseId()).orElseThrow( () -> new ApiException("Course not found", HttpStatus.NOT_FOUND));
            User user = userRepository.findById(wishListDto.getUserId()).orElseThrow( () -> new ApiException("User not found", HttpStatus.NOT_FOUND));
            log.info("Product received by userId -: " + wishListDto.getUserId());

            boolean alreadyExists = wishListRepository.existsByUserIdAndCourseId(user, course);
            if (alreadyExists) {
                throw new ApiException("Course already exists in the wishlist", HttpStatus.ALREADY_REPORTED);
            }

            WishList wishListInstance = WishList.builder()
                    .userId(user)
                    .courseId(course)
                    .build();

            wishListRepository.save(wishListInstance);
            log.info("Product added to wishlist successfully");

        } catch (Exception e) {
            throw new Exception(e);
        }

    }
}
