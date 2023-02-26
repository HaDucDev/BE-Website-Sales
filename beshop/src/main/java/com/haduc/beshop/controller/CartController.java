package com.haduc.beshop.controller;

import com.haduc.beshop.service.ICartService;
import com.haduc.beshop.util.dto.request.user.CartRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @PostMapping
    public ResponseEntity<?> addProductToCart(@RequestBody CartRequest cartRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iCartService.addProductToCart(cartRequest));
    }
}
