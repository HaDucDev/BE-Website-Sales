package com.haduc.beshop.controller;


import com.haduc.beshop.model.Cart;
import com.haduc.beshop.model.Order;
import com.haduc.beshop.service.IOrderService;
import com.haduc.beshop.util.dto.request.user.CreateOrderResquest;
import com.haduc.beshop.util.dto.request.user.MomoIPNRequest;
import com.haduc.beshop.util.dto.request.user.OrderConfirmationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {


    @Autowired
    private IOrderService iOrderService;

    @PostMapping("/order-confirmation")//kiem tra validate
    public ResponseEntity<?> checkProductOrderConfirmation(@Valid @RequestBody OrderConfirmationRequest orderConfirmationRequest) {
        System.out.println(" Mang Du Lieu");
        orderConfirmationRequest.getProductIdBuyList().forEach((i) -> System.out.println(i));
        return ResponseEntity.status(HttpStatus.OK).body(this.iOrderService.checkProductOrderConfirmation(orderConfirmationRequest));
    }


    @GetMapping("/order-confirmation/{userId}")//load don hang
    public ResponseEntity<?> loadOrderComfirm(@PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iOrderService.loadOrderComfirm(userId));
    }

    // thanh toan bang tien mat tao don hang hoac tra ve link thanh toan online
    @PostMapping("/create-offline-or-link-payment-online")
    public ResponseEntity<?> createOrderVsOffline(@RequestBody CreateOrderResquest createOrderResquest) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(this.iOrderService.createOrderVsOfflineOrLinkTransferPayment(createOrderResquest));
    }

    // neu link thanh toan online sau khi thuc hien thi momo se trả ve du lieu o day
    @PostMapping(path = "/result-payment-online-momo", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void handleMomoIPN(MomoIPNRequest request){
        this.iOrderService.handleOrderAfterPaymentMoMo(request);
    }

    //lay tât ca don hang của nguoi dung
    @GetMapping("/all-order/{userId}")
    public ResponseEntity<List<Order>> getAllOrderByIdOfUser(@PathVariable Integer userId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iOrderService.findAllByUser_UserId(userId));
    }
}
