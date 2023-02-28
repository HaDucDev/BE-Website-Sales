package com.haduc.beshop.controller;


import com.haduc.beshop.model.Supplier;
import com.haduc.beshop.service.ISupplierService;
import com.haduc.beshop.util.dto.request.admin.CreateSupplierRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateSupplierRequest;
import com.haduc.beshop.util.dto.response.admin.GetSupplierResponse;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private ISupplierService iSupplierService ;


    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSupplier() {
        return  ResponseEntity.status(HttpStatus.OK).body(this.iSupplierService.getAllSupplier());
    }
    @Secured({"ROLE_ADMIN"})
    @GetMapping("/admin/{id}")
    public ResponseEntity<GetSupplierResponse> getSupplierById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iSupplierService.findBySupplierIdAndIsDeleteFalse(id));
    }
    @Secured({"ROLE_ADMIN"})
    @PostMapping(value = "/admin",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> createSupplier( @Valid @RequestPart("createSupplierRequest") CreateSupplierRequest createSupplierRequest
            ,  @RequestPart(value = "supplierFile",required = false) MultipartFile supplierFile) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iSupplierService.createSupplier(createSupplierRequest,supplierFile));
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping(value = "/admin",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> updateSupplier( @Valid @RequestPart("updateSupplierRequest") UpdateSupplierRequest UpdateSupplierRequest
            ,  @RequestPart(value = "supplierFile",required = false) MultipartFile supplierFile) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iSupplierService.updateSupplier(UpdateSupplierRequest,supplierFile));
    }
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<MessageResponse> deleteSupplier(@PathVariable Integer id) {
        this.iSupplierService.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Supplier với id = '" + id + "' đã được xóa thành công"));
    }
}
