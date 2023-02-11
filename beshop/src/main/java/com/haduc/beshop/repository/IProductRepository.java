package com.haduc.beshop.repository;

import com.haduc.beshop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> {

    //admin
    List<Product> findAllByIsDeleteFalse();

    Optional<Product> findByProductIdAndIsDeleteFalse(Integer productId);//Optional dẻ dung orElseThrow

    // them,sua dung ham co san

    // xoa mem
    @Modifying
    @Query("UPDATE Product t SET t.isDelete = true WHERE t.productId = :id AND t.isDelete = false")
    int softDeleteProduct(@Param("id") Integer id);

    //user
    Page<Product> findAllByIsDeleteFalse(Pageable pageable);
}
