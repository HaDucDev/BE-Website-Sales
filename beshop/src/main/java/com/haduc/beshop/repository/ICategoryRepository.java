package com.haduc.beshop.repository;

import com.haduc.beshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Integer> {

    //admin
    List<Category> findAllByIsDeleteFalse();

    Optional<Category> findByCategoryIdAndIsDeleteFalse(Integer categoryId);

    // them,sua dung ham co san

    // xoa mem
    @Modifying
    @Query("UPDATE Category t SET t.isDelete = true WHERE t.categoryId = :id AND t.isDelete = false")
    int softDeleteCategory(@Param("id") Integer id);
}
