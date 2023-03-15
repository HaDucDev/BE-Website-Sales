package com.haduc.beshop.controller;


import com.haduc.beshop.service.IAccountService;
import com.haduc.beshop.util.dto.request.account.*;
import com.haduc.beshop.util.dto.response.account.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class AccountController {

    @Autowired
    private IAccountService iAccountService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iAccountService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iAccountService.register(request));
    }

    @Secured({"ROLE_ADMIN","ROLE_CUSTOMER","ROLE_SHIPPER"})
    @PutMapping("/change-pass")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iAccountService.changePass(request));
    }

    @Secured({"ROLE_ADMIN","ROLE_CUSTOMER","ROLE_SHIPPER"})
    @PutMapping("/change-account-info")
    public ResponseEntity<?> changeInforAccount(@Valid @RequestPart("changeInforAccountRequest") ChangeInforAccountRequest changeInforAccountRequest
            , @RequestPart(value = "avatar",required = false) MultipartFile avatar) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iAccountService.updateInforUser(changeInforAccountRequest,avatar));
    }


    @PostMapping("/forget-send-code")
    public ResponseEntity<?> ForgetPassSendTokenCode(@Valid @RequestBody ForgetPasswordRequest forgetPasswordRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iAccountService.createtTokenCodeWhenFortgetPass(forgetPasswordRequest));
    }

    @PostMapping("/confirm-code-send-new-pass")
    public ResponseEntity<?> ForgetPassCheckCodeConfirm(@Valid @RequestBody SetPasswordRandomRequest setPasswordRandomRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iAccountService.setPasswordRandomAndSendNewPassMail(setPasswordRandomRequest));
    }
}
