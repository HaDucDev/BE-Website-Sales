package com.haduc.beshop.repository;


import com.haduc.beshop.model.OrderDetail;
import com.haduc.beshop.model.OrderDetailIDKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailIDKey> {

    //them ctdh dung ham so san

    //lay tat chi tiet tiet don theo id
    List<OrderDetail> findAllById_OrdersId(Integer id);

    //cap nhat isreview để không cho nó hiển thị đánh giá khi đã đánh giá rồi
    @Modifying
    @Query("UPDATE OrderDetail t SET t.isReview= :isReview  WHERE t.id= :id ")
    int updateDisableReviewOrderDetail(@Param("isReview") String isReview, @Param("id") OrderDetailIDKey id);

}
