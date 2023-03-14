package com.haduc.beshop.repository;

import com.haduc.beshop.model.Product;
import com.haduc.beshop.util.dto.response.admin.ColumnChartDataResponse;
import com.haduc.beshop.util.dto.response.user.GetManysupplierBuyCategory;
import com.haduc.beshop.util.dto.response.user.ProductSellingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface IProductRepository extends JpaRepository<Product, Integer> , JpaSpecificationExecutor<Product> {

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

     // search -filter- da dung spring data Specification
//    @Query("SELECT p FROM Product p WHERE p.isDelete = false AND (:categoryId IS NULL OR p.category.categoryId = :categoryId) " +
//            "AND (:supplierId IS NULL OR p.supplier.supplierId = :supplierId)  " +
//            "AND (:text IS NULL OR p.productName LIKE %:text%)")
//    Page<Product> searchFilterProducts(@Param("categoryId") Integer categoryId, @Param("supplierId") Integer supplierId, @Param("text") String text, Pageable pageable);


    //lay xem loai san pham co bao nhieu hang ban. vi du may tinh co HP, MSI
    @Query("SELECT DISTINCT new com.haduc.beshop.util.dto.response.user.GetManysupplierBuyCategory(s.supplierId, s.supplierName) " +
            "FROM Product p " +
            "JOIN Category c ON p.category.categoryId = c.categoryId " +
            "JOIN Supplier s ON p.supplier.supplierId = s.supplierId " +
            "WHERE c.categoryId = :categoryId")
    List<GetManysupplierBuyCategory> getProductByCategoryManySupplier(@Param("categoryId") Integer categoryId);

    // lấy ra 10 sản phẩm bản chạy nhất
    @Query("SELECT new com.haduc.beshop.util.dto.response.user.ProductSellingResponse(p.productId, p.productName,p.productImage,p.unitPrice,p.discount,p.rating, sum (od.quantity)) " +
            "FROM Product p JOIN OrderDetail od ON p.productId = od.product.productId "
            + "group by p.productId order by sum (od.quantity) desc")
    List<ProductSellingResponse> getTop10ProductSelling();

}
