package com.haduc.beshop.service;

import com.haduc.beshop.util.dto.request.account.ChangeInforAccountRequest;
import com.haduc.beshop.util.dto.request.account.ChangePasswordRequest;
import com.haduc.beshop.util.dto.request.account.LoginRequest;
import com.haduc.beshop.util.dto.request.account.RegisterRequest;
import com.haduc.beshop.util.dto.request.admin.CreateUserRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateProductRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateUserRequest;
import com.haduc.beshop.util.dto.response.account.LoginResponse;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IAccountService {
    LoginResponse login(LoginRequest request);

    MessageResponse register(RegisterRequest registerRequest);

    MessageResponse changePass(ChangePasswordRequest changePasswordRequest);

    MessageResponse updateInforUser(ChangeInforAccountRequest changeInforAccountRequest, MultipartFile productFile);
}
