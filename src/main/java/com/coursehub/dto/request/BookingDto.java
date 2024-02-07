package com.coursehub.dto.request;

import com.coursehub.model.Course;
import com.coursehub.model.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
public class BookingDto {

    @Getter
    private Long user;

    @Getter
    private Long course;

    @Getter
    private Double totalCost;
}
