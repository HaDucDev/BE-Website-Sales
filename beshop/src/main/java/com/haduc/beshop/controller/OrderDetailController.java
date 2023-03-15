package com.haduc.beshop.controller;

import com.haduc.beshop.model.Order;
import com.haduc.beshop.model.OrderDetail;
import com.haduc.beshop.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order-detail")
public class OrderDetailController {

    @Autowired
    private IOrderDetailService iOrderDetailService;

    @Secured({"ROLE_ADMIN","ROLE_CUSTOMER","ROLE_SHIPPER"})
    @GetMapping("/all-order/{ordersId}")
    public ResponseEntity<List<OrderDetail>> getAllOrderDetailByOrderId(@PathVariable Integer ordersId) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iOrderDetailService.findAllById_OrdersId(ordersId));
    }

}
