package com.haduc.beshop.util.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewsRequest {

    private Integer userId;
    private Integer productId;
    private Integer ordersId;

    @NotBlank(message = "nội dung đánh giá không được để trống")
    private String comment;

    @Min(value = 1, message = "Đánh giá được nhỏ hơn 1")
    @Max(value = 5, message = "Đánh giá được lớn hơn 5")
    private Integer rating;

}
