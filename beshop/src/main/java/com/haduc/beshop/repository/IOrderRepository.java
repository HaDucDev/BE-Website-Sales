package com.haduc.beshop.repository;


import com.haduc.beshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order,Integer> {

    Optional<Order> findById(Integer id);

    List<Order> findAllByUser_UserId(Integer id);

}
