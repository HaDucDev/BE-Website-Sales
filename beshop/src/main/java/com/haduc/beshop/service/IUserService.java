package com.haduc.beshop.service;

import com.haduc.beshop.model.User;

import java.util.List;

public interface IUserService {

    //admin
    //* lay tat ca user
    List<User> getAllUser();

    //GetSupplierResponse findBySupplierIdAndIsDeleteFalse(Integer supplierId);

    //MessageResponse createSupplier(CreateSupplierRequest createSupplierRequest, MultipartFile supplierFile);

    //MessageResponse updateSupplier(UpdateSupplierRequest updateSupplierRequest, MultipartFile supplierFile);

    //void deleteById(Integer id);
}
