package com.haduc.beshop.service;

import com.haduc.beshop.util.dto.request.user.OrderConfirmationRequest;
import com.haduc.beshop.util.dto.response.user.GetLoadOrderComfirmResponse;

public interface IOrderService {

    boolean checkProductOrderConfirmation (OrderConfirmationRequest orderConfirmationRequest);//validate

    GetLoadOrderComfirmResponse loadOrderComfirm(Integer userId);



}
