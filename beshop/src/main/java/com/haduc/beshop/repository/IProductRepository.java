package com.haduc.beshop.repository;

import com.haduc.beshop.model.Product;
import com.haduc.beshop.util.dto.response.admin.ColumnChartDataResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    //cap nhat so star sau khi binh luan
    @Modifying
    @Query("UPDATE Product t SET t.rating = :rating WHERE t.productId = :id AND t.isDelete = false")
    int updateStartProduct(@Param("rating") Double rating,@Param("id") Integer id);


    // thong ke doanh thu theo san pham
        @Query(" SELECT new com.haduc.beshop.util.dto.response.admin.ColumnChartDataResponse(p.productName, SUM (od.amount)) " +
            " FROM Product p JOIN OrderDetail od  ON p.productId = od.product.productId" +
                " JOIN Order o ON  od.order.ordersId = o.ordersId" +
            " GROUP BY p.productId ")
    List<ColumnChartDataResponse> getRevenueStatistics();

     // search -filter
     Page<Product> findAllByIsDeleteFalseAndCategory_CategoryIdAndSupplier_SupplierIdAndProductNameLike(Integer categoryId, Integer supplierId,
                                                                                                        String text,Pageable pageable);
    Page<Product> findAllByIsDeleteFalseAndCategory_CategoryIdAndProductNameLike(Integer categoryId, String text,Pageable pageable);
    Page<Product> findAllByIsDeleteFalseAndSupplier_SupplierIdAndProductNameLike(Integer supplierId, String text,Pageable pageable);

}
