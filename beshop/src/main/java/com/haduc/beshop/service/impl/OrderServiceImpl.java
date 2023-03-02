package com.haduc.beshop.service.impl;


import com.haduc.beshop.model.Product;
import com.haduc.beshop.repository.IProductRepository;
import com.haduc.beshop.service.IOrderService;
import com.haduc.beshop.util.dto.request.user.OrderConfirmationRequest;
import com.haduc.beshop.util.exception.NotXException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IProductRepository iProductRepository;

    @Override
    public boolean checkProductOrderConfirmation(OrderConfirmationRequest orderConfirmationRequest) {
        List<Integer> productList = orderConfirmationRequest.getProductIdBuyList();// lay list id product
        productList.stream().map((id)->
            this.iProductRepository.findByProductIdAndIsDeleteFalse(id)
                    .orElseThrow(()-> new NotXException("Sanr phẩm này không tồn tại", HttpStatus.NOT_FOUND)))
                    .collect(Collectors.toList());
        return true;
    }
}
