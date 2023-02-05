package com.haduc.beshop.repository;

import com.haduc.beshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

    //admin
    List<User> findAllByIsDeleteFalse();

    Optional<User> findByUserIdAndIsDeleteFalse(Integer userId);//Optional dẻ dung orElseThrow

    // them,sua dung ham co san

    // xoa mem
    @Modifying
    @Query("UPDATE User t SET t.isDelete = true WHERE t.userId = :id AND t.isDelete = false")
    int softDeleteUser(@Param("id") Integer id);

}
