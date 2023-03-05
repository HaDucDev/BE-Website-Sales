package com.haduc.beshop.repository;


import com.haduc.beshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order,Integer> {

    Optional<Order> findById(Integer id);

    List<Order> findAllByUser_UserId(Integer id);

    // dung chung cap nhat cac trang thai don hang luon
    @Modifying
    @Query("UPDATE Order t SET t.statusOrder =:message  WHERE t.ordersId = :id ")
    int softUpdateStatusOrder(@Param("id") Integer id, @Param("message") String message);

}
