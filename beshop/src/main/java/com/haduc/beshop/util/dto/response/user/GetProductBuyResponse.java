package com.haduc.beshop.util.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductBuyResponse {
    private Integer productId;
    private String productName;
    private Integer quantityBuy;
    private String productImage;
    private Integer discount;
    private Integer unitPrice;

    private Integer sellingPrice=0;
    private Integer totalMoney=0;
}
