package com.haduc.beshop.service;

import com.haduc.beshop.model.Category;
import com.haduc.beshop.model.Supplier;
import com.haduc.beshop.util.payload.request.admin.CreateCategoryRequest;
import com.haduc.beshop.util.payload.request.admin.CreateSupplierRequest;
import com.haduc.beshop.util.payload.request.admin.UpdateCategoryRequest;
import com.haduc.beshop.util.payload.request.admin.UpdateSupplierRequest;
import com.haduc.beshop.util.payload.response.admin.GetCategoryResponse;
import com.haduc.beshop.util.payload.response.admin.GetSupplierResponse;
import com.haduc.beshop.util.payload.response.admin.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISupplierService {

    //admin

    //* lay tat ca san pham con co trong kho
    List<Supplier> getAllSupplier();

    GetSupplierResponse findBySupplierIdAndIsDeleteFalse(Integer supplierId);

    MessageResponse createSupplier(CreateSupplierRequest createSupplierRequest, MultipartFile supplierFile);

    MessageResponse updateSupplier(UpdateSupplierRequest updateSupplierRequest);

    void deleteById(Integer id);
}
