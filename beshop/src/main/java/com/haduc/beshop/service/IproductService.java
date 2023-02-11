package com.haduc.beshop.service;

import com.haduc.beshop.model.Product;
import com.haduc.beshop.util.dto.request.admin.CreateProductRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateProductRequest;
import com.haduc.beshop.util.dto.response.admin.GetProductDetailResponse;
import com.haduc.beshop.util.dto.response.admin.MessageResponse;
import com.haduc.beshop.util.dto.response.user.GetProductsPaginationResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IproductService {

    //admin

    //* lay tat ca san pham con co trong kho
    List<Product> getAllProduct();

    GetProductDetailResponse findByProductIdAndIsDeleteFalse(Integer productId);

    MessageResponse createProduct(CreateProductRequest createProductRequest, MultipartFile productFile);

    MessageResponse updateProduct(UpdateProductRequest updateProductRequest, MultipartFile productFile);

    void deleteById(Integer id);

    //user
    GetProductsPaginationResponse getAllProductAndIsDeleteFalsePagination(Pageable pageable);
}
