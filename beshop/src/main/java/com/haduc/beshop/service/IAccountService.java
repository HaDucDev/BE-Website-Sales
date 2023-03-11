package com.haduc.beshop.service;

import com.haduc.beshop.util.dto.request.account.*;
import com.haduc.beshop.util.dto.response.account.LoginResponse;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface IAccountService {
    LoginResponse login(LoginRequest request);

    MessageResponse register(RegisterRequest registerRequest);

    MessageResponse changePass(ChangePasswordRequest changePasswordRequest);

    MessageResponse updateInforUser(ChangeInforAccountRequest changeInforAccountRequest, MultipartFile productFile);

    MessageResponse createtTokenCodeWhenFortgetPass ( ForgetPasswordRequest forgetPasswordRequest);
    MessageResponse setPasswordRandomAndSendNewPassMail(SetPasswordRandomRequest setPasswordRandomRequest);
}
