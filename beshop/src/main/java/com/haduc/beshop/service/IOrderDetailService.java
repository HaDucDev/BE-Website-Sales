package com.haduc.beshop.service;

import com.haduc.beshop.model.OrderDetail;

import java.util.List;

public interface IOrderDetailService {

    List<OrderDetail> findAllById_OrdersId(Integer id);
}
