package com.coursehub.controllers;


import com.coursehub.dto.request.WishListDto;
import com.coursehub.model.WishList;
import com.coursehub.service.WishlistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wishlist")
@CrossOrigin()
public class WishListController {

    private final WishlistService wishlistService;

    public WishListController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public Map<String, String> addProductToWishList(@RequestBody WishListDto wishListDto) throws Exception {
        wishlistService.addCourseToWishListByUser(wishListDto);
        return Map.of("response", "Course Added to Wishlist successfully");
    }

    @ResponseStatus(HttpStatus.FOUND)
    @GetMapping("{userId}")
    public List<WishList> getAllProductByUserWishListId(@PathVariable(value = "userId") Long userId) {
        return wishlistService.getAllCourseByUserWishListId(userId);
    }


    @DeleteMapping("/{userId}/courses/{courseId}")
    public ResponseEntity<?> removeCourseFromWishlist(@PathVariable Long userId, @PathVariable Long courseId) {
        wishlistService.deleteCourseFromWishlist(userId, courseId);
        return ResponseEntity.ok().build();
    }

}
