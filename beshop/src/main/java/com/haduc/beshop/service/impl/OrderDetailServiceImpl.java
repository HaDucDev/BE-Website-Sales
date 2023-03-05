package com.haduc.beshop.service.impl;


import com.haduc.beshop.model.OrderDetail;
import com.haduc.beshop.repository.IOrderDetailRepository;
import com.haduc.beshop.service.IOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements IOrderDetailService {

    @Autowired
    private IOrderDetailRepository iOrderDetailRepository;


    @Override
    public List<OrderDetail> findAllById_OrdersId(Integer id) {
        return this.iOrderDetailRepository.findAllById_OrdersId(id);
    }
}
