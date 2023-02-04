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
public class UpdateCategoryRequest {

    @NotNull(message = "Lỗi id loại hàng")
    private Integer categoryId;

    @NotBlank(message = "Tên loại hàng không được để trống")
    private String categoryName;
}
