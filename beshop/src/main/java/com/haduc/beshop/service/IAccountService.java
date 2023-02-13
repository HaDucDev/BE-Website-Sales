package com.haduc.beshop.service;

import com.haduc.beshop.util.dto.request.account.LoginRequest;
import com.haduc.beshop.util.dto.response.account.LoginResponse;

public interface IAccountService {
    LoginResponse login(LoginRequest request);
}
