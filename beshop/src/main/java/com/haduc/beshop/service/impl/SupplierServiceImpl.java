package com.haduc.beshop.service.impl;


import com.haduc.beshop.config.AmazonConfigService;
import com.haduc.beshop.model.Supplier;
import com.haduc.beshop.repository.ISupplierRepository;
import com.haduc.beshop.service.ISupplierService;
import com.haduc.beshop.util.exception.NotXException;
import com.haduc.beshop.util.dto.request.admin.CreateSupplierRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateSupplierRequest;
import com.haduc.beshop.util.dto.response.admin.GetSupplierResponse;
import com.haduc.beshop.util.dto.response.admin.MessageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    public MessageResponse createSupplier(CreateSupplierRequest createSupplierRequest, MultipartFile supplierFile) {
        Supplier supplier = new Supplier();
        supplier.setSupplierName(createSupplierRequest.getSupplierName());
        if (supplierFile == null || supplierFile.isEmpty()==true){
            supplier.setSupplierImage("https://res.cloudinary.com/dyatpgcxn/image/upload/v1670474470/oavh6rbwonghakquh8fo.jpg");
        }
        else {
            String image=amazonConfigService.uploadFile(supplierFile);
            supplier.setSupplierImage(image);
        }
        Supplier saveSupplier= this.iSupplierRepository.save(supplier);
        return new MessageResponse(String.format("Supplier %s được tạo thành công!", saveSupplier.getSupplierName()));
    }

    @Override
    public MessageResponse updateSupplier(UpdateSupplierRequest updateSupplierRequest, MultipartFile supplierFile) {
        Supplier supplier =  this.iSupplierRepository.findBySupplierIdAndIsDeleteFalse(updateSupplierRequest.getSupplierId())
                .orElseThrow(() -> new NotXException("Không tìm thấy supplier này", HttpStatus.NOT_FOUND));
        supplier.setSupplierName(updateSupplierRequest.getSupplierName());
        if(supplierFile != null || supplierFile.isEmpty()==false){
            String image=amazonConfigService.uploadFile(supplierFile);
            supplier.setSupplierImage(image);
        }
        if (supplierFile == null || supplierFile.isEmpty()==true) {
            supplier.setSupplierImage(supplier.getSupplierImage());
        }
        Supplier saveSupplier= this.iSupplierRepository.save(supplier);
        return new MessageResponse(String.format("Supplier có id là %s được cập nhật thành công!", saveSupplier.getSupplierId().toString()));
    }

    @Override
    public void deleteById(Integer id) {

    }
}
