package com.haduc.beshop.util.dto.response.account;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {

    //private String jwt;
    private Integer userId;
    private String username;
    private String roleName;
    private Integer roleId;
}
