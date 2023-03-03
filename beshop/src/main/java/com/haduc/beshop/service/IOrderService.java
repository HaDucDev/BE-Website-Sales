package com.haduc.beshop.service;

import com.haduc.beshop.util.dto.request.user.CreateOrderResquest;
import com.haduc.beshop.util.dto.request.user.OrderConfirmationRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.dto.response.user.GetLoadOrderComfirmResponse;
import com.mservice.allinone.models.CaptureMoMoResponse;

public interface IOrderService {

    boolean checkProductOrderConfirmation (OrderConfirmationRequest orderConfirmationRequest);//validate

    GetLoadOrderComfirmResponse loadOrderComfirm(Integer userId);

    CaptureMoMoResponse createOrderVsOfflineOrLinkTransferPayment(CreateOrderResquest createOrderResquest) throws Exception;



}
