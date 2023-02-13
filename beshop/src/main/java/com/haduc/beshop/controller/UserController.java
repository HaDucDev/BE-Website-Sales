package com.haduc.beshop.controller;

import com.haduc.beshop.model.User;
import com.haduc.beshop.service.IUserService;
import com.haduc.beshop.util.dto.response.admin.GetUserResponse;
import com.haduc.beshop.util.dto.response.admin.GetUsersPaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private IUserService iUserService;


    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUser() {
        return  ResponseEntity.status(HttpStatus.OK).body(this.iUserService.getAllUser());
    }
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/admin")
    public ResponseEntity<GetUsersPaginationResponse> findAllUsers
            (@RequestParam(defaultValue = "0") int number, @RequestParam(defaultValue = "6") int size, @PageableDefault(sort = "userId") Sort sort) {
        Pageable paging = PageRequest.of(number, size,sort);
        return ResponseEntity.ok(this.iUserService.getAllUserAndIsDeleteFalsePagination(paging));
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iUserService.findByUserIdAndIsDeleteFalse(id));
    }
}
