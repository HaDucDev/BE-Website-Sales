package com.haduc.beshop.repository;

import com.haduc.beshop.model.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IProductRepository {

    //admin
    List<Product> findAllByIsDeleteFalse();

    Optional<Product> findByProductIdAndIsDeleteFalse(Integer productId);//Optional dẻ dung orElseThrow

    // them,sua dung ham co san

    // xoa mem
    @Modifying
    @Query("UPDATE Product t SET t.isDelete = true WHERE t.productId = :id AND t.isDelete = false")
    int softDeleteProduct(@Param("id") Integer id);
}
