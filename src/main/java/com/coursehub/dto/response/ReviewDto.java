package com.coursehub.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewDto {


    private Long course;

    private Long user;

    private String comment;

    private int rating;


    public ReviewDto(Long course, Long user, String comment, int rating) {
        this.course = course;
        this.user = user;
        this.comment = comment;
        this.rating = rating;
    }
}
