package com.coursehub.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WishListDto {

    private Long userId;

    private Long CourseId;
}
