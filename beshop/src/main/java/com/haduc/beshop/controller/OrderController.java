package com.haduc.beshop.controller;



import com.haduc.beshop.service.IOrderService;
import com.haduc.beshop.util.dto.request.user.OrderConfirmationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {


    @Autowired
    private IOrderService iOrderService;
    @PostMapping("/order-confirmation")
    public ResponseEntity<?> checkProductOrderConfirmation(@Valid @RequestBody OrderConfirmationRequest orderConfirmationRequest) {
        System.out.println(" Mang Du Lieu");
        orderConfirmationRequest.getProductIdBuyList().forEach((i)-> System.out.println(i));
        return ResponseEntity.status(HttpStatus.OK).body(this.iOrderService.checkProductOrderConfirmation(orderConfirmationRequest));
    }

}
