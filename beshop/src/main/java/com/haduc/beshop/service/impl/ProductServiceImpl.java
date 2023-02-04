package com.haduc.beshop.service.impl;

import com.haduc.beshop.model.Product;
import com.haduc.beshop.repository.IProductRepository;
import com.haduc.beshop.service.IproductService;
import com.haduc.beshop.util.dto.request.admin.CreateProductRequest;
import com.haduc.beshop.util.dto.response.admin.GetCategoryResponse;
import com.haduc.beshop.util.dto.response.admin.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IproductService {

    @Autowired
    private IProductRepository iProductRepository;

    @Override
    public List<Product> getAllProduct() {
        return this.iProductRepository.findAllByIsDeleteFalse();
    }

    @Override
    public GetCategoryResponse findByProductIdAndIsDeleteFalse(Integer productId) {
        return null;
    }

    @Override
    public MessageResponse createProduct(CreateProductRequest createProductRequest) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }
}
