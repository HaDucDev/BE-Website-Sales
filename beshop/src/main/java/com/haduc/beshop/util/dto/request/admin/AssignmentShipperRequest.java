package com.haduc.beshop.util.dto.request.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentShipperRequest {

    @NotBlank(message = "Id đơn hàng không được để trống")
    private Integer ordersId;
}
