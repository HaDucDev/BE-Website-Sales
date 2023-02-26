package com.haduc.beshop.util.dto.request.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {

    private Integer userId;
    private Integer productId;
    private Integer quantity;
}
