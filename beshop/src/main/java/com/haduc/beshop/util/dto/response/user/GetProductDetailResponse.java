package com.haduc.beshop.util.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductDetailResponse {

    private String productName;

    private Integer quantity;

    private String productImage;

    private Integer discount;

    private Integer unitPrice;

    private String descriptionProduct;

    private String isCategory;

    private String isSupplier;

    private Double rating;// sao trung binh cua san pham
}
