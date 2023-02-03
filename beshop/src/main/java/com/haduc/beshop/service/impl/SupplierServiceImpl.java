package com.haduc.beshop.service.impl;


import com.haduc.beshop.model.Supplier;
import com.haduc.beshop.repository.ISupplierRepository;
import com.haduc.beshop.service.ISupplierService;
import com.haduc.beshop.util.payload.request.admin.CreateSupplierRequest;
import com.haduc.beshop.util.payload.request.admin.UpdateSupplierRequest;
import com.haduc.beshop.util.payload.response.admin.GetCategoryResponse;
import com.haduc.beshop.util.payload.response.admin.GetSupplierResponse;
import com.haduc.beshop.util.payload.response.admin.MessageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierServiceImpl implements ISupplierService {

    @Autowired
    private ISupplierRepository iSupplierRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Supplier> getAllSupplier() {
        return this.iSupplierRepository.findAllByIsDeleteFalse();
    }

    @Override
    public GetSupplierResponse findBySupplierIdAndIsDeleteFalse(Integer supplierId) {
        return this.modelMapper.map(this.iSupplierRepository.findBySupplierIdAndIsDeleteFalse(supplierId), GetSupplierResponse.class);
    }

    @Override
    public MessageResponse createSupplier(CreateSupplierRequest createSupplierRequest) {
        return null;
    }

    @Override
    public MessageResponse updateSupplier(UpdateSupplierRequest updateSupplierRequest) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }
}
