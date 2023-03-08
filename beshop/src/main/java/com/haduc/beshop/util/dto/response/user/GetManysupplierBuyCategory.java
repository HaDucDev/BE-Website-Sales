package com.haduc.beshop.util.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetManysupplierBuyCategory {

    private Integer supplierId;
    private String supplierName;
}
