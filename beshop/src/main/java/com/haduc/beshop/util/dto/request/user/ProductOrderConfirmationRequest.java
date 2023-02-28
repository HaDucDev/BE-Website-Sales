package com.haduc.beshop.util.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderConfirmationRequest {

    private Integer productId;

    private Integer quantity;
}
