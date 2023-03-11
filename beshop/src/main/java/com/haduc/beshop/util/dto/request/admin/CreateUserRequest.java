package com.haduc.beshop.util.dto.request.admin;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    private String email;
    private String fullName;
    private String username;
    private String address;
    private String phone;
    private Integer roleId;
}
