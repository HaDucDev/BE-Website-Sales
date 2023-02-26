package com.haduc.beshop.repository;


import com.haduc.beshop.model.Cart;
import com.haduc.beshop.model.CartIDKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<Cart,CartIDKey> {

    //tim san pham trong gio
    //Optional<Cart> findById_ProductIdAndId_UserIdAndIsDeleteFalse(Integer productId, Integer userId);

    //xoa san pham khoi cart
    @Modifying
    @Query("UPDATE Cart c SET c.isDelete = true WHERE c.id = :id AND c.isDelete = false")
    int deleteProductFromCart(@Param("id") CartIDKey cartIDKey);

}
