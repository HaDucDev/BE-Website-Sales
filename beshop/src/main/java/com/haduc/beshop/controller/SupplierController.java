package com.haduc.beshop.controller;


import com.haduc.beshop.model.Category;
import com.haduc.beshop.model.Supplier;
import com.haduc.beshop.service.ICategoryService;
import com.haduc.beshop.service.ISupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private ISupplierService iSupplierService ;


    @GetMapping
    public ResponseEntity<List<Supplier>> getAllCategory() {
        return  ResponseEntity.status(HttpStatus.OK).body(this.iSupplierService.getAllSupplier());
    }
}
