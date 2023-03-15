package com.haduc.beshop.controller;

import com.haduc.beshop.model.Role;
import com.haduc.beshop.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private IRoleService iRoleService;


    // lay tat ca quyen khi them nguoi dung
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/admin")
    public ResponseEntity<List<Role>> getAllRole() {
        return ResponseEntity.status(HttpStatus.OK).body(this.iRoleService.getAllRole());
    }
}
