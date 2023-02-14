package com.haduc.beshop.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.haduc.beshop.model.Product;
import com.haduc.beshop.repository.ICategoryRepository;
import com.haduc.beshop.repository.IProductRepository;
import com.haduc.beshop.repository.ISupplierRepository;
import com.haduc.beshop.service.IproductService;
import com.haduc.beshop.util.dto.request.admin.CreateProductRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateProductRequest;
import com.haduc.beshop.util.dto.response.admin.GetProductAdminResponse;
import com.haduc.beshop.util.dto.response.admin.MessageResponse;
import com.haduc.beshop.util.dto.response.user.GetProductsPaginationResponse;
import com.haduc.beshop.util.exception.NotXException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private Cloudinary cloudinary;

    @Override
    public List<Product> getAllProduct() {
        return this.iProductRepository.findAllByIsDeleteFalse();
    }



    @Override
    public GetProductAdminResponse findByProductIdAndIsDeleteFalse(Integer productId) {
        Product product= this.iProductRepository.findByProductIdAndIsDeleteFalse(productId)
                .orElseThrow(()->new NotXException("Không tìm thấy product này", HttpStatus.NOT_FOUND));
        GetProductAdminResponse getProductDetailResponse = this.modelMapper.map(product, GetProductAdminResponse.class);
        return getProductDetailResponse;
    }

    @Override
    public MessageResponse createProduct(CreateProductRequest createProductRequest, MultipartFile productFile) {
        Product product= this.modelMapper.map(createProductRequest,Product.class);
        product.setCategory(this.iCategoryRepository.findByCategoryIdAndIsDeleteFalse(createProductRequest.getCategoryId()).get());
        product.setSupplier(this.iSupplierRepository.findBySupplierIdAndIsDeleteFalse(createProductRequest.getSupplierId())
                .orElseThrow(()-> new NotXException("Không tìm thấy product này", HttpStatus.NOT_FOUND)));
        if (productFile == null || productFile.isEmpty()){
            product.setProductImage("https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg");
        }
        else {
            try {
                Map p = this.cloudinary.uploader().upload(productFile.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                String image= (String) p.get("secure_url");
                product.setProductImage(image);
            }
            catch (IOException e) {
                System.out.println("loi post add supplier" + e.getMessage());
            }
        }

        Product productSave= this.iProductRepository.save(product);
        return new MessageResponse(String.format("Product %s được tạo thành công!", productSave.getProductName()));
    }

    @Override
    public MessageResponse updateProduct(UpdateProductRequest updateProductRequest, MultipartFile productFile) {
        Product getProductData = this.iProductRepository.findByProductIdAndIsDeleteFalse(updateProductRequest.getProductId())
                .orElseThrow(() -> new NotXException("Không tìm thấy product này", HttpStatus.NOT_FOUND));
        getProductData.setProductName(updateProductRequest.getProductName());
        getProductData.setQuantity(updateProductRequest.getQuantity());
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

            try {
                Map p = this.cloudinary.uploader().upload(productFile.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                String image= (String) p.get("secure_url");
                getProductData.setProductImage(image);
            }
            catch (IOException e) {
                System.out.println("loi post update product" + e.getMessage());
            }
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

    //user
    @Override
    public GetProductsPaginationResponse getAllProductAndIsDeleteFalsePagination(Pageable pageable) {
        Page<Product> productPage= this.iProductRepository.findAllByIsDeleteFalse(pageable);

        GetProductsPaginationResponse getUsersPaginationResponse = this.modelMapper.map(productPage,GetProductsPaginationResponse.class);// lay 4 thuoc duoi ko co content
        // convert tung phan tu trong list.
        getUsersPaginationResponse.setContent(productPage.getContent().stream().map(product -> this.modelMapper.map(product, com.haduc.beshop.util.dto.response.user.GetProductResponse.class)).collect(Collectors.toList()));

        return getUsersPaginationResponse;
    }

    //chi tiet san pham
//    @Override
//    public Get findByProductIdAndIsDeleteFalse(Integer productId) {
//        Product product= this.iProductRepository.findByProductIdAndIsDeleteFalse(productId)
//                .orElseThrow(()->new NotXException("Không tìm thấy product này", HttpStatus.NOT_FOUND));
//        GetProductDetailResponse getProductDetailResponse = this.modelMapper.map(product, GetProductDetailResponse.class);
//        getProductDetailResponse.setIsCategory(product.getCategory().getCategoryName());
//        getProductDetailResponse.setIsSupplier(product.getSupplier().getSupplierName());
//        return getProductDetailResponse;
//    }

}
