package com.haduc.beshop.service;

import com.haduc.beshop.util.dto.request.account.LoginRequest;
import com.haduc.beshop.util.dto.response.account.LoginResponse;
import com.haduc.beshop.util.exception.LoginException;

public interface IAccountService {
    LoginResponse login(LoginRequest request) throws LoginException;
}
