package com.haduc.beshop.util.dto.request.user;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderConfirmationRequest {

    @NotEmpty(message = "Bạn chưa chọn sản phẩm nào để đặt hàng cả")
    private List<Integer> productIdBuyList;
}
