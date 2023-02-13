package com.haduc.beshop.controller;


import com.haduc.beshop.service.IAccountService;
import com.haduc.beshop.util.dto.request.account.LoginRequest;
import com.haduc.beshop.util.dto.response.account.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class AccountController {

    @Autowired
    private IAccountService iAccountService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iAccountService.login(request));
    }
}
