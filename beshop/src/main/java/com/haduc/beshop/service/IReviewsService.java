package com.haduc.beshop.service;

import com.haduc.beshop.model.Reviews;
import com.haduc.beshop.util.dto.request.user.ReviewsRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;

import java.util.List;

public interface IReviewsService {

    MessageResponse addReviewsToProduct(ReviewsRequest reviewsRequest);

    List<Reviews> findById_ProductId(Integer productId);

}
