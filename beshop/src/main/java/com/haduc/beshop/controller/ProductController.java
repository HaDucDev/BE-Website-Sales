package com.haduc.beshop.controller;

import com.haduc.beshop.model.Product;
import com.haduc.beshop.service.IproductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)// phep các bat ki api nao goi
@RestController
@RequestMapping("/api/admin/product")
public class ProductController {

    @Autowired
    private IproductService iproductService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllSupplier() {
        return  ResponseEntity.status(HttpStatus.OK).body(this.iproductService.getAllProduct());
    }
}
