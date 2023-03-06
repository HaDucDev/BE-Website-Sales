package com.haduc.beshop.repository;

import com.haduc.beshop.model.Reviews;
import com.haduc.beshop.model.ReviewsIdKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReviewsRepository extends JpaRepository<Reviews, ReviewsIdKey> {

    // add them review comment su dung ham co san
}
