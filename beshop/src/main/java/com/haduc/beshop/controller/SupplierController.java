package com.haduc.beshop.controller;


import com.haduc.beshop.model.Supplier;
import com.haduc.beshop.service.ISupplierService;
import com.haduc.beshop.util.payload.response.admin.GetSupplierResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/supplier")
public class SupplierController {

    @Autowired
    private ISupplierService iSupplierService ;


    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSupplier() {
        return  ResponseEntity.status(HttpStatus.OK).body(this.iSupplierService.getAllSupplier());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetSupplierResponse> getSupplierById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iSupplierService.findBySupplierIdAndIsDeleteFalse(id));
    }
}
