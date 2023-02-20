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

    private String jwtToken;
    private Integer userId;
    private String username;
    private String roleName;
    private Integer roleId;
    private String avatar;
}
