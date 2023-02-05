package com.haduc.beshop.controller;

import com.haduc.beshop.model.User;
import com.haduc.beshop.service.IUserService;
import com.haduc.beshop.util.dto.response.admin.GetProductResponse;
import com.haduc.beshop.util.dto.response.admin.GetUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/user")
public class UserController {

    @Autowired
    private IUserService iUserService;


    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        return  ResponseEntity.status(HttpStatus.OK).body(this.iUserService.getAllUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iUserService.findByUserIdAndIsDeleteFalse(id));
    }
}
