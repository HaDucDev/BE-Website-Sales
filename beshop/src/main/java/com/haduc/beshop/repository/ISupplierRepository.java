package com.haduc.beshop.repository;

import com.haduc.beshop.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ISupplierRepository extends JpaRepository<Supplier, Integer> {

    List<Supplier> findAllByIsDeleteFalse();

    Optional<Supplier> findBySupplierIdAndIsDeleteFalse(Integer supplierId);

    // them,sua dung ham co san

    // xoa mem
    @Modifying
    @Query("UPDATE Supplier t SET t.isDelete = true WHERE t.supplierId = :id AND t.isDelete = false")
    int softDeleteSupplier(@Param("id") Integer id);
}
