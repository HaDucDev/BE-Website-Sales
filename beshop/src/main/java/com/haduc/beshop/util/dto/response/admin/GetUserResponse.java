package com.haduc.beshop.util.dto.response.admin;

import com.haduc.beshop.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponse {

    private int userId;

    private String avatar;

    private String username;

    private String address;

    private String fullName;

    private String email;

    //private String password;

    private String phone;

    private String roleName;
}
