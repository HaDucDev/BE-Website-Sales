package com.haduc.beshop.controller;



import com.haduc.beshop.util.dto.request.user.OrderConfirmationRequest;
import com.haduc.beshop.util.dto.request.user.ProductOrderConfirmationRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.exception.NotXException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {


    @PostMapping("/order-confirmation")
    public ResponseEntity<?> getAllProduct(@Valid @RequestBody OrderConfirmationRequest orderConfirmationRequest) {

        List<ProductOrderConfirmationRequest> list = orderConfirmationRequest.getProductIdBuyList();
        for( ProductOrderConfirmationRequest i: list){
            if(i.getQuantity()==0){
                throw new NotXException("Số lượng sản phẩm không được bé thua một", HttpStatus.BAD_REQUEST);
            }
            System.out.println(i.getProductId() + "-" + i.getQuantity());
        }
        return ResponseEntity.ok(new MessageResponse("Sản phẩm thành công"));
    }

}
