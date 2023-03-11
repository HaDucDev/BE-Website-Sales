package com.haduc.beshop.util.dto.request.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeInforAccountRequest {

    private String username;
    private String email;
    private String fullName;
    private String address;
    private String phone;



}
