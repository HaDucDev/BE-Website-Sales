package com.haduc.beshop.repository;

import com.haduc.beshop.model.Cart;
import com.haduc.beshop.model.Reviews;
import com.haduc.beshop.model.ReviewsIdKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReviewsRepository extends JpaRepository<Reviews, ReviewsIdKey> {

    // add them review comment su dung ham co san

    //lay tat ca binh luan boi san pham
    List<Reviews> findById_ProductId(Integer productId);

}
