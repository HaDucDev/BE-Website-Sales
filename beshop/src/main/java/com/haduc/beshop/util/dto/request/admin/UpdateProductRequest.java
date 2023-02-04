package com.haduc.beshop.util.dto.request.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {

    @NotNull(message = "Lỗi id sản phẩm")
    private Integer productId;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String productName;

    @NotNull(message = "Số lượng sản phẩm không được để trống")
    @Min(value = 1, message = "Số lượng sản phẩm không được nhỏ hơn 1")
    private Integer quantity;

    //private String productImage;
    @NotNull(message = "mã giảm giá không được để trống")
    @Min(value = 0, message = "Mã giảm giá sản phẩm không được nhỏ hơn 0%")
    @Max(value = 100, message = "Mã giảm giá sản phẩm không được lớn hơn 100%")
    private Integer discount;

    @NotNull(message = "Giá sản phẩm sản phẩm không được để trống")
    @Min(value = 1, message = "Giá sản phẩm không được nhỏ hơn 1")
    private Integer unitPrice;

    @NotBlank(message = "Đặc tả sản phẩm không được để trống")
    private String descriptionProduct;

    //private Integer rating=0;// sao trung binh cua san pham

    @NotNull(message = "Chưa chọn loại hàng")
    private Integer categoryId;

    @NotNull(message = "Chưa chọn hãng")
    private Integer supplierId;
}
