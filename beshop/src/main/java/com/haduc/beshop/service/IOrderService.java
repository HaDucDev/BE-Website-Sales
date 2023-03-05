package com.haduc.beshop.service;

import com.haduc.beshop.model.Order;
import com.haduc.beshop.util.dto.request.user.CreateOrderResquest;
import com.haduc.beshop.util.dto.request.user.MomoIPNRequest;
import com.haduc.beshop.util.dto.request.user.OrderConfirmationRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.dto.response.user.GetLoadOrderComfirmResponse;

import java.util.List;

public interface IOrderService {
    //==================================================> USER
    boolean checkProductOrderConfirmation (OrderConfirmationRequest orderConfirmationRequest);//validate

    GetLoadOrderComfirmResponse loadOrderComfirm(Integer userId);// data trang xac nhan

    MessageResponse createOrderVsOfflineOrLinkTransferPayment(CreateOrderResquest createOrderResquest) throws Exception;// tao dan hang


    void handleOrderAfterPaymentMoMo(MomoIPNRequest request);

    List<Order> findAllByUser_UserId(Integer id);// lay don hang cua nguoi dung
    void deleteById(Integer id);


    //===============================================> ADMIN
    List<Order> findAllOrderByCreatedDateDesc();

}
