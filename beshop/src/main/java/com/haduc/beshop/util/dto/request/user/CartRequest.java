package com.haduc.beshop.util.dto.request.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {

    private Integer userId;
    private Integer productId;

    @Min(value = 1, message = "Số lượng sản phẩm dặt mua không được nhỏ hơn 1")
    private Integer quantity;

    @NotBlank(message = "phép thực hiện không được để trống")
    private String operator;
}
