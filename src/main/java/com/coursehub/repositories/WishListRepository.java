package com.coursehub.repositories;

import com.coursehub.model.Course;
import com.coursehub.model.User;
import com.coursehub.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    @Query(
            value = "SELECT w FROM WishList w WHERE w.userId = :userId"
    )
    List<WishList> findAllWishListById(User userId);


    boolean existsByUserIdAndCourseId(User userId, Course courseId);

    void deleteByUserIdAndCourseId(User userId, Course courseId);
}