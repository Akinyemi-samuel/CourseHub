package com.coursehub.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter private Long courseId;
    @Getter private String name;
    @Getter private String instructor;
    @Getter private String duration;
    @Getter private String date;
    @Getter private String time;
    @Getter private String location;
    @Getter private double price;
    @Getter private String description;
    @Getter private String category;
    @Getter private String imageUrl;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    @Getter private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<Booking> bookings = new ArrayList<>();
}
