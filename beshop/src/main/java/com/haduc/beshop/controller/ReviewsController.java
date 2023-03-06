package com.haduc.beshop.controller;


import com.haduc.beshop.service.IReviewsService;
import com.haduc.beshop.util.dto.request.user.CartRequest;
import com.haduc.beshop.util.dto.request.user.ReviewsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/reviews")
public class ReviewsController {

    @Autowired
    private IReviewsService iReviewsService;

    @PostMapping
    public ResponseEntity<?> addReviewsToProduct(@Valid @RequestBody ReviewsRequest reviewsRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iReviewsService.addReviewsToProduct(reviewsRequest));
    }

}
