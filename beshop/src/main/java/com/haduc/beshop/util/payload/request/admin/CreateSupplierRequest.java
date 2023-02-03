package com.haduc.beshop.util.payload.request.admin;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateSupplierRequest {

    @NotBlank(message = "Tên nhà sản xuất không được để trống")
    private String supplierName;

    private MultipartFile supplierFile;// file null thì lấy mặc định thôi
}
