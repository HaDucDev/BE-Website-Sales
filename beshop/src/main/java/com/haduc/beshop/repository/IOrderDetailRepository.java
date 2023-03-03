package com.haduc.beshop.repository;


import com.haduc.beshop.model.OrderDetail;
import com.haduc.beshop.model.OrderDetailIDKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailIDKey> {
}
