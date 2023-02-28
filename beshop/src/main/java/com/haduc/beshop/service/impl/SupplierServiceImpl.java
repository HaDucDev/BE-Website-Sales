package com.haduc.beshop.service.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.haduc.beshop.model.Supplier;
import com.haduc.beshop.repository.ISupplierRepository;
import com.haduc.beshop.service.ISupplierService;
import com.haduc.beshop.util.exception.NotXException;
import com.haduc.beshop.util.dto.request.admin.CreateSupplierRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateSupplierRequest;
import com.haduc.beshop.util.dto.response.admin.GetSupplierResponse;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class SupplierServiceImpl implements ISupplierService {

    @Autowired
    private ISupplierRepository iSupplierRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Cloudinary cloudinary;

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
            try {
                Map p = this.cloudinary.uploader().upload(supplierFile.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                String image= (String) p.get("secure_url");
                supplier.setSupplierImage(image);
            }
            catch (IOException e) {
                System.out.println("loi post add supplier" + e.getMessage());
            }
        }
        Supplier saveSupplier= this.iSupplierRepository.save(supplier);
        return new MessageResponse(String.format("Supplier %s được tạo thành công!", saveSupplier.getSupplierName()));
    }

    @Override
    public MessageResponse updateSupplier(UpdateSupplierRequest updateSupplierRequest, MultipartFile supplierFile) {
        Supplier supplier =  this.iSupplierRepository.findBySupplierIdAndIsDeleteFalse(updateSupplierRequest.getSupplierId())
                .orElseThrow(() -> new NotXException("Không tìm thấy supplier này", HttpStatus.NOT_FOUND));
        supplier.setSupplierName(updateSupplierRequest.getSupplierName());

        if (supplierFile == null || supplierFile.isEmpty()==true){
            supplier.setSupplierImage(supplier.getSupplierImage());
        }
        else {
            try {
                Map p = this.cloudinary.uploader().upload(supplierFile.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                String image= (String) p.get("secure_url");
                supplier.setSupplierImage(image);
            }
            catch (IOException e) {
                System.out.println("loi put supplier" + e.getMessage());
            }
        }
        Supplier saveSupplier= this.iSupplierRepository.save(supplier);
        return new MessageResponse(String.format("Supplier có id là %s được cập nhật thành công!", saveSupplier.getSupplierId().toString()));
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        int affectedRows = this.iSupplierRepository.softDeleteSupplier(id);
        System.out.println(affectedRows);
        if (affectedRows == 0) {
            throw new NotXException("Xảy ra lỗi khi xóa supplier", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
