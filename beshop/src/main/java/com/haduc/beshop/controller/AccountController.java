package com.haduc.beshop.controller;


import com.haduc.beshop.service.IAccountService;
import com.haduc.beshop.util.dto.request.account.ChangeInforAccountRequest;
import com.haduc.beshop.util.dto.request.account.ChangePasswordRequest;
import com.haduc.beshop.util.dto.request.account.LoginRequest;
import com.haduc.beshop.util.dto.request.account.RegisterRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateProductRequest;
import com.haduc.beshop.util.dto.response.account.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/change-pass")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iAccountService.changePass(request));
    }

    @PutMapping("/change-account-info")
    public ResponseEntity<?> changeInforAccount(@Valid @RequestPart("changeInforAccountRequest") ChangeInforAccountRequest changeInforAccountRequest
            , @RequestPart(value = "avatar",required = false) MultipartFile avatar) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iAccountService.updateInforUser(changeInforAccountRequest,avatar));
    }
}
