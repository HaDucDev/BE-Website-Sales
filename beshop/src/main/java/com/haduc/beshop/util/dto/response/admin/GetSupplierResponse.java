package com.haduc.beshop.util.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetSupplierResponse {

    private Integer supplierId;

    private String supplierName;

    private String supplierImage;

    private boolean isDelete;

}
