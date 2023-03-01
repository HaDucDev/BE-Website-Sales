package com.haduc.beshop.repository;


import com.haduc.beshop.model.Cart;
import com.haduc.beshop.model.CartIDKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartRepository extends JpaRepository<Cart,CartIDKey> {

    //tim san pham trong gio cua nguoi dung
    List<Cart> findById_UserIdAndIsDeleteFalse(Integer userId);

    //dem so loai san pham co trong gio
    Long countById_UserIdAndIsDeleteFalse(Integer userId);

    //xoa san pham khoi cart
    @Modifying
    @Query("UPDATE Cart c SET c.isDelete = true WHERE c.id = :id AND c.isDelete = false")
    int deleteProductFromCart(@Param("id") CartIDKey cartIDKey);

    Cart findByIdAndIsDeleteFalse(CartIDKey id);

}
