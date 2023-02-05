package com.haduc.beshop.controller;

import com.haduc.beshop.model.User;
import com.haduc.beshop.service.IUserService;
import com.haduc.beshop.util.dto.response.admin.GetProductResponse;
import com.haduc.beshop.util.dto.response.admin.GetUserResponse;
import com.haduc.beshop.util.dto.response.admin.GetUsersPaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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


    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUser() {
        return  ResponseEntity.status(HttpStatus.OK).body(this.iUserService.getAllUser());
    }

    @GetMapping
    public ResponseEntity<GetUsersPaginationResponse> findAllUsers(@PageableDefault(sort = "userId") Pageable pageable) {
        return ResponseEntity.ok(this.iUserService.getAllUserAndIsDeleteFalsePagination(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iUserService.findByUserIdAndIsDeleteFalse(id));
    }
}
