package com.haduc.beshop.service;

import com.haduc.beshop.model.Product;
import com.haduc.beshop.util.dto.request.admin.CreateProductRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateProductRequest;
import com.haduc.beshop.util.dto.response.admin.GetProductResponse;
import com.haduc.beshop.util.dto.response.admin.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IproductService {

    //admin

    //* lay tat ca san pham con co trong kho
    List<Product> getAllProduct();

    GetProductResponse findByProductIdAndIsDeleteFalse(Integer productId);

    MessageResponse createProduct(CreateProductRequest createProductRequest, MultipartFile productFile);

    MessageResponse updateProduct(UpdateProductRequest updateProductRequest, MultipartFile productFile);

    void deleteById(Integer id);
}
