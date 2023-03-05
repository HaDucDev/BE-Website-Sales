package com.haduc.beshop.util.dto.response.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetLoadOrderComfirmResponse {

    private String fullName;
    private String phone;
    private String address;
    List<GetProductBuyResponse> getProductBuyResponseList;
}
