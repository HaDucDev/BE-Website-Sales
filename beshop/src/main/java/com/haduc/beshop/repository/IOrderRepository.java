package com.haduc.beshop.repository;


import com.haduc.beshop.model.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    //===================================> ADMIN
    List<Order> findAll(Sort sort);

    // cap nhat shipper duoc phan cong giao hang
    @Modifying
    @Query("UPDATE Order t SET t.shipperId =:shipperId , t.statusOrder=:status  WHERE t.ordersId = :id ")
    int softUpdateAssignmentOrder( @Param("shipperId") Integer shipperId, @Param("status") String status, @Param("id") Integer id);


    //======================================> SHIPPER
    List<Order> findAllByShipperId(Integer id);

    @Modifying
    @Query("UPDATE Order t SET t.statusOrder = :message, t.receivedDate= :receivedDate  WHERE t.ordersId = :id and t.shipperId= :shipperId")
    int softUpdateCompleteOrder(@Param("id") Integer id , @Param("receivedDate") Date receivedDate, @Param("message") String message, @Param("shipperId") Integer shipperId);

     //tim don hang de binh luan -truy van nay sai
    //Optional<Order> findByIdAndStatusOrderEquals(Integer id, String statusOrder);
}
