package com.haduc.beshop.util.dto.response.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSellingResponse {

    private Integer productId;
    private String productName;
    private String productImage;
    private Integer unitPrice;
    private Integer discount;
    private Double rating;
    private  Long sumQuantity;
}
