package com.haduc.beshop.util.dto.request.shipper;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmOrderRequest {

    private Integer shipperId;
    private Integer orderId;
}
