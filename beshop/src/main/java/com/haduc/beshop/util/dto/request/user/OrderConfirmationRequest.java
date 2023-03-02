package com.haduc.beshop.util.dto.request.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderConfirmationRequest {

    @NotEmpty(message = "Bạn chưa chọn sản phẩm nào để đặt hàng cả")
    private List<Integer> productIdBuyList;
}
