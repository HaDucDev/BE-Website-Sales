package com.haduc.beshop.repository;


import com.haduc.beshop.model.Order;
import com.haduc.beshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order,Integer> {

    Optional<Order> findById(Integer id);

}
