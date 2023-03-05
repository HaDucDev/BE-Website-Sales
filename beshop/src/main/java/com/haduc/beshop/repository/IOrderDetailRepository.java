package com.haduc.beshop.repository;


import com.haduc.beshop.model.OrderDetail;
import com.haduc.beshop.model.OrderDetailIDKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailIDKey> {

    //them ctdh dung ham so san

    //lay tat chi tiet tiet don theo id
    List<OrderDetail> findAllById_OrdersId(Integer id);
}
