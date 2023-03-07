package com.haduc.beshop.util.dto.response.admin;


import com.haduc.beshop.model.Category;
import com.haduc.beshop.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductAdminResponse {

    private Integer productId;
    private String productName;
    private Integer quantity;
    private String productImage;
    private Integer discount;
    private Integer unitPrice;
    private String descriptionProduct;
    private boolean isDelete= Boolean.FALSE;
    private Double rating;// sao trung binh cua san pham
    private Category category;
    private Supplier supplier;
}
