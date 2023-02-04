package com.haduc.beshop.util.dto.request.admin;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSupplierRequest {

    @NotNull(message = "Lỗi id nhà sản xuất")
    private Integer supplierId;

    @NotBlank(message = "Tên nhà sản xuất không được để trống")
    private String supplierName;

    //private MultipartFile supplierFile;// file null thì lấy mặc định thôi
}
