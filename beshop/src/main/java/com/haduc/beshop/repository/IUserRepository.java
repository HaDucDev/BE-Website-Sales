package com.haduc.beshop.repository;

import com.haduc.beshop.model.User;
import com.haduc.beshop.util.enum_role.ERole;
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
public interface IUserRepository extends JpaRepository<User, Integer> {

    //common
    Optional<User> findByUsername(String username);// use java 8

    Optional<User> findByUsernameAndIsDeleteFalse(String username);// use java 8

    //admin
    List<User> findAllByIsDeleteFalse();

    Page<User> findAllByIsDeleteFalse(Pageable pageable);

    Optional<User> findByUserIdAndIsDeleteFalse(Integer userId);//Optional dẻ dung orElseThrow

    // them,sua dung ham co san

    // xoa mem
    @Modifying
    @Query("UPDATE User t SET t.isDelete = true WHERE t.userId = :id AND t.isDelete = false")
    int softDeleteUser(@Param("id") Integer id);

    //lay tat ca user theo mot role nao do
    List<User> findByRole_NameAndAssignment(ERole name, Integer assignment);

    //cap nhat cot phan cong cua bang
    @Modifying
    @Query("UPDATE User t SET t.assignment = :assignment")
    int updateColumnAssignment(@Param("assignment") Integer assignment);


    //cap nhat cot phan cong cua bang
    @Modifying
    @Query("UPDATE User t SET t.assignment = :assignment WHERE t.userId = :userId")
    int updateAfterAssignment(@Param("assignment") Integer assignment, @Param("userId") Integer userId);

    //shipper khong nhan don - chuyen sang shipper khac
    //lay tat ca shipper tru shipper co id
    List<User> findByRole_NameAndAssignmentAndUserIdNot(ERole name, Integer assignment, Integer userId);


    //lay nguoi dung vs email de tao token doi mat khau
    Optional<User> findByEmailAndIsDeleteFalse(String email);// use java 8

}
