package com.haduc.beshop.service;

import com.haduc.beshop.util.dto.request.user.ReviewsRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;

public interface IReviewsService {

    MessageResponse addReviewsToProduct(ReviewsRequest reviewsRequest);

}
