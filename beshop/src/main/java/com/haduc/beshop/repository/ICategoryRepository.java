package com.haduc.beshop.repository;

import com.haduc.beshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findAllByIsDeleteFalse();

    Optional<Category> findByCategoryIdAndIsDeleteFalse(Integer categoryId);
}
