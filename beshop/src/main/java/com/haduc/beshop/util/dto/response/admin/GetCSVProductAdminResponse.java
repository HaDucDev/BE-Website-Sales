package com.haduc.beshop.util.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCSVProductAdminResponse {
    private String productId;
    private String productName;
    private String quantity;
    private String productImage;
    private String discount;
    private String unitPrice;
    private String descriptionProduct;
    private String isDelete;
    private String rating;// sao trung binh cua san pham
    private String categoryId;
    private String supplierId;
}
