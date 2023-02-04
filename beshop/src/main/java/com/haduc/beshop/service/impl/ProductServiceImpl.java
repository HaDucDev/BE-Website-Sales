package com.haduc.beshop.service.impl;

import com.haduc.beshop.config.AmazonConfigService;
import com.haduc.beshop.model.Product;
import com.haduc.beshop.repository.ICategoryRepository;
import com.haduc.beshop.repository.IProductRepository;
import com.haduc.beshop.repository.ISupplierRepository;
import com.haduc.beshop.service.IproductService;
import com.haduc.beshop.util.dto.request.admin.CreateProductRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateProductRequest;
import com.haduc.beshop.util.dto.response.admin.GetProductResponse;
import com.haduc.beshop.util.dto.response.admin.MessageResponse;
import com.haduc.beshop.util.exception.NotXException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private AmazonConfigService amazonConfigService;

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
    public MessageResponse createProduct(CreateProductRequest createProductRequest, MultipartFile productFile) {
        Product product= this.modelMapper.map(createProductRequest,Product.class);
        product.setCategory(this.iCategoryRepository.findByCategoryIdAndIsDeleteFalse(createProductRequest.getCategoryId()).get());
        product.setSupplier(this.iSupplierRepository.findBySupplierIdAndIsDeleteFalse(createProductRequest.getSupplierId())
                .orElseThrow(()-> new NotXException("Không tìm thấy supplier này", HttpStatus.NOT_FOUND)));
        if (productFile == null || productFile.isEmpty()==true){
            product.setProductImage("https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg");
        }
        else {
            String image=amazonConfigService.uploadFile(productFile);
            product.setProductImage(image);
        }

        Product productSave= this.iProductRepository.save(product);
        return new MessageResponse(String.format("Product %s được tạo thành công!", productSave.getProductName()));
    }

    @Override
    public MessageResponse updateProduct(UpdateProductRequest updateProductRequest, MultipartFile productFile) {
        Product getProductData = this.iProductRepository.findByProductIdAndIsDeleteFalse(updateProductRequest.getProductId())
                .orElseThrow(() -> new NotXException("Không tìm thấy product này", HttpStatus.NOT_FOUND));
        getProductData.setProductName(updateProductRequest.getProductName());
        getProductData.setQuantity(updateProductRequest.getQuantity());;
        getProductData.setDiscount(updateProductRequest.getDiscount());
        getProductData.setUnitPrice(updateProductRequest.getUnitPrice());
        getProductData.setDescriptionProduct(updateProductRequest.getDescriptionProduct());
        getProductData.setCategory(this.iCategoryRepository.findByCategoryIdAndIsDeleteFalse(updateProductRequest.getCategoryId())
                .orElseThrow(()-> new NotXException("Không tìm thấy cate này", HttpStatus.NOT_FOUND)));
        getProductData.setSupplier(this.iSupplierRepository.findBySupplierIdAndIsDeleteFalse(updateProductRequest.getSupplierId())
                .orElseThrow(()-> new NotXException("Không tìm thấy supplier này", HttpStatus.NOT_FOUND)));

        if (productFile == null || productFile.isEmpty()==true){
            getProductData.setProductImage(getProductData.getProductImage());
        }
        else {
            String image=amazonConfigService.uploadFile(productFile);
            getProductData.setProductImage(image);
        }

        Product productSave= this.iProductRepository.save(getProductData);
        return new MessageResponse(String.format("Product %s được sửa thành công!", productSave.getProductName()));
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        int affectedRows = this.iProductRepository.softDeleteProduct(id);
        System.out.println(affectedRows);
        if (affectedRows == 0) {
            throw new NotXException("Xảy ra lỗi khi xóa supplier", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
