package com.haduc.beshop.controller;

import com.haduc.beshop.model.Cart;
import com.haduc.beshop.model.CartIDKey;
import com.haduc.beshop.service.ICartService;
import com.haduc.beshop.util.dto.request.user.CartRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private ICartService iCartService;

    @GetMapping("/all-product/{id}")
    public ResponseEntity<List<Cart>> getAllProduct(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iCartService.getAllProductFromCartByUserId(id));
    }

    @GetMapping("/count-cart/{userId}")
    public ResponseEntity<?> countProductCategory(@PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iCartService.countById_UserIdAndIsDeleteFalse(userId));
    }



    @PostMapping
    public ResponseEntity<?> addProductToCart(@RequestBody CartRequest cartRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iCartService.addProductToCart(cartRequest));
    }

    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteProductFromCart(@RequestBody CartIDKey cartIDKey) {
        this.iCartService.deleteById(cartIDKey);
        return ResponseEntity.ok(new MessageResponse("product với id = '" + cartIDKey.getProductId() + "' đã được xóa thành công khỏi giỏ hàng"));
    }
}
