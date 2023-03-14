package com.haduc.beshop.service;

import com.haduc.beshop.model.Product;
import com.haduc.beshop.util.dto.request.admin.CreateProductRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateProductRequest;
import com.haduc.beshop.util.dto.response.admin.GetProductAdminResponse;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.dto.response.user.GetManysupplierBuyCategory;
import com.haduc.beshop.util.dto.response.user.GetProductDetailResponse;
import com.haduc.beshop.util.dto.response.user.GetProductsPaginationResponse;
import com.haduc.beshop.util.dto.response.user.ProductSellingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IproductService {

    //admin

    //* lay tat ca san pham con co trong kho
    List<Product> getAllProduct();

    GetProductAdminResponse findByProductIdAndIsDeleteFalse(Integer productId);

    MessageResponse createProduct(CreateProductRequest createProductRequest, MultipartFile productFile);

    MessageResponse updateProduct(UpdateProductRequest updateProductRequest, MultipartFile productFile);

    void deleteById(Integer id);

    //user
    GetProductsPaginationResponse getAllProductAndIsDeleteFalsePagination(Pageable pageable);

    GetProductDetailResponse findByProductDetalAndIsDeleteFalse(Integer productId);// chi tiet san pham

    //ham de dung tim kiem va filter api chung
    GetProductsPaginationResponse searchFilterProductsNew(Integer categoryId, Integer supplierId, String text, List<String> priceString, Pageable pageable) ;

    List<GetManysupplierBuyCategory> getProductByCategoryManySupplier(Integer categoryId);

    //top 10 san pham ban chay nhat
    List<ProductSellingResponse> getTop10ProductSelling();




}
