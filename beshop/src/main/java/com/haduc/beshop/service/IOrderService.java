package com.haduc.beshop.service;

import com.haduc.beshop.model.Order;
import com.haduc.beshop.util.dto.request.user.CreateOrderResquest;
import com.haduc.beshop.util.dto.request.user.MomoIPNRequest;
import com.haduc.beshop.util.dto.request.user.OrderConfirmationRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.dto.response.user.GetLoadOrderComfirmResponse;

import java.util.List;

public interface IOrderService {

    boolean checkProductOrderConfirmation (OrderConfirmationRequest orderConfirmationRequest);//validate

    GetLoadOrderComfirmResponse loadOrderComfirm(Integer userId);

    MessageResponse createOrderVsOfflineOrLinkTransferPayment(CreateOrderResquest createOrderResquest) throws Exception;


    void handleOrderAfterPaymentMoMo(MomoIPNRequest request);

    List<Order> findAllByUser_UserId(Integer id);

}
