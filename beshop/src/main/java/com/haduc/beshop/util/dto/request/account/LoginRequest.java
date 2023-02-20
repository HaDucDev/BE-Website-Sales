package com.haduc.beshop.util.dto.request.account;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    //@Pattern(regexp = "^[A-Za-z0-9]{3,16}$", message = "Tên đăng nhập phải là các chứ hoa, chữ thường, số, chiều dài lớn hơn 3")
    private String username;


    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 3, max=20, message = "Chiều dài mật khẩu phải lớn hơn 4 kí tự, tối đa 20 kí tự")
    private String password;
}
