package com.haduc.beshop.service.impl;

import com.haduc.beshop.model.Product;
import com.haduc.beshop.repository.ICategoryRepository;
import com.haduc.beshop.repository.IProductRepository;
import com.haduc.beshop.repository.ISupplierRepository;
import com.haduc.beshop.service.IproductService;
import com.haduc.beshop.util.dto.request.admin.CreateProductRequest;
import com.haduc.beshop.util.dto.response.admin.GetProductResponse;
import com.haduc.beshop.util.dto.response.admin.MessageResponse;
import com.haduc.beshop.util.exception.NotXException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IproductService {

    @Autowired
    private IProductRepository iProductRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ICategoryRepository iCategoryRepository;

    @Autowired
    private ISupplierRepository iSupplierRepository;

    @Override
    public List<Product> getAllProduct() {
        return this.iProductRepository.findAllByIsDeleteFalse();
    }

    @Override
    public GetProductResponse findByProductIdAndIsDeleteFalse(Integer productId) {
        Product product= this.iProductRepository.findByProductIdAndIsDeleteFalse(productId)
                .orElseThrow(()->new NotXException("Không tìm thấy supplier này", HttpStatus.NOT_FOUND));
        GetProductResponse getProductResponse = this.modelMapper.map(product,GetProductResponse.class);
        getProductResponse.setIsCategory(product.getCategory().getCategoryName());
        getProductResponse.setIsSupplier(product.getSupplier().getSupplierName());
        return getProductResponse;
    }

    @Override
    public MessageResponse createProduct(CreateProductRequest createProductRequest) {
        Product product= this.modelMapper.map(createProductRequest,Product.class);
        product.setCategory(this.iCategoryRepository.findByCategoryIdAndIsDeleteFalse(createProductRequest.getCategoryId()).get());
        product.setSupplier(this.iSupplierRepository.findBySupplierIdAndIsDeleteFalse(createProductRequest.getSupplierId())
                .orElseThrow(()-> new NotXException("Không tìm thấy supplier này", HttpStatus.NOT_FOUND)));
        Product productSave= this.iProductRepository.save(product);
        return new MessageResponse(String.format("Product %s được tạo thành công!", productSave.getProductName()));
    }

    @Override
    public void deleteById(Integer id) {

    }
}
