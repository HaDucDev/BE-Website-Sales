package com.haduc.beshop.service;

import com.haduc.beshop.model.Product;
import com.haduc.beshop.util.dto.request.admin.CreateProductRequest;
import com.haduc.beshop.util.dto.response.admin.GetCategoryResponse;
import com.haduc.beshop.util.dto.response.admin.MessageResponse;

import java.util.List;

public interface IproductService {

    //admin

    //* lay tat ca san pham con co trong kho
    List<Product> getAllProduct();

    GetCategoryResponse findByProductIdAndIsDeleteFalse(Integer productId);

    MessageResponse createProduct(CreateProductRequest createProductRequest);

   // MessageResponse updateProduct(UpdateProductRequest updateProductRequest);

    void deleteById(Integer id);
}
