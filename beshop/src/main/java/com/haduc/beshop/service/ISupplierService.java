package com.haduc.beshop.service;

import com.haduc.beshop.model.Supplier;
import com.haduc.beshop.util.dto.request.admin.CreateSupplierRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateSupplierRequest;
import com.haduc.beshop.util.dto.response.admin.GetSupplierResponse;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ISupplierService {

    //admin

    //* lay tat ca nha sx con co trong kho
    List<Supplier> getAllSupplier();

    GetSupplierResponse findBySupplierIdAndIsDeleteFalse(Integer supplierId);

    MessageResponse createSupplier(CreateSupplierRequest createSupplierRequest, MultipartFile supplierFile);

    MessageResponse updateSupplier(UpdateSupplierRequest updateSupplierRequest, MultipartFile supplierFile);

    void deleteById(Integer id);
}
