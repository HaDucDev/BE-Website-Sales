package com.haduc.beshop.service;

import com.haduc.beshop.util.dto.request.user.OrderConfirmationRequest;

public interface IOrderService {

    boolean checkProductOrderConfirmation (OrderConfirmationRequest orderConfirmationRequest);
}
