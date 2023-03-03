package com.haduc.beshop.util.dto.request.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderResquest {

    private Integer userId;
    private String receiptUser;
    private String phone;
    private String address;
    private String methodPayment;
    List<Integer> buyProducts;
}
