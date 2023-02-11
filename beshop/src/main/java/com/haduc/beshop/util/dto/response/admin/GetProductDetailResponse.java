package com.haduc.beshop.util.dto.response.admin;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductDetailResponse {// getById-admin


    private String productName;

    private Integer quantity;

    private String productImage;

    private Integer discount;

    private Integer unitPrice;

    private String descriptionProduct;

    private String isCategory;

    private String isSupplier;

}
