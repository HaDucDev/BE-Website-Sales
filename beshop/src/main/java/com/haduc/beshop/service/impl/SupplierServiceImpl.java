package com.haduc.beshop.service.impl;


import com.haduc.beshop.config.AmazonConfigService;
import com.haduc.beshop.model.Category;
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

    @Autowired
    private AmazonConfigService amazonConfigService;

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
        Supplier supplier = new Supplier();
        supplier.setSupplierName(createSupplierRequest.getSupplierName());
        if (createSupplierRequest.getSupplierFile() == null || createSupplierRequest.getSupplierFile().isEmpty()==true){
            supplier.setSupplierImage("https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg");
        }
        else {
            String image=amazonConfigService.uploadFile(createSupplierRequest.getSupplierFile());
            supplier.setSupplierImage(image);
        }
        Supplier saveSupplier= this.iSupplierRepository.save(supplier);
        return new MessageResponse(String.format("Supplier %s được tạo thành công!", saveSupplier.getSupplierName()));
    }

    @Override
    public MessageResponse updateSupplier(UpdateSupplierRequest updateSupplierRequest) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }
}
