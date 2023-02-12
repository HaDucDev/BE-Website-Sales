package com.haduc.beshop.util.dto.response.user;

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
public class GetProductResponse {

    private Integer productId;

    private String productName;

    private Integer quantity;

    private String productImage;

    private Integer discount;

    private Integer unitPrice;

    private String descriptionProduct;

    private Integer rating=0;

    private Category category;

    private Supplier supplier;

}
