package com.haduc.beshop.util.dto.request.account;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RegisterRequest {
    private String email;
    private String fullName;
    private String username;
    private String address;
    private String phone;
    //private Integer roleId;//luon la customer
}
