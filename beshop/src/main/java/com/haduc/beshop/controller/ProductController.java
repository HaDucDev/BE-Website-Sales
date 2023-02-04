package com.haduc.beshop.controller;

import com.haduc.beshop.model.Product;
import com.haduc.beshop.service.IproductService;
import com.haduc.beshop.util.dto.request.admin.CreateProductRequest;
import com.haduc.beshop.util.dto.request.admin.CreateSupplierRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateProductRequest;
import com.haduc.beshop.util.dto.response.admin.GetProductResponse;
import com.haduc.beshop.util.dto.response.admin.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)// phep các bat ki api nao goi
@RestController
@RequestMapping("/api/admin/product")
public class ProductController {

    @Autowired
    private IproductService iproductService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProduct() {
        return  ResponseEntity.status(HttpStatus.OK).body(this.iproductService.getAllProduct());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetProductResponse> getProductById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iproductService.findByProductIdAndIsDeleteFalse(id));
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createProduct(@Valid @RequestPart("createProductRequest") CreateProductRequest createProductRequest
            , @RequestPart("productFile")MultipartFile productFile) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iproductService.createProduct(createProductRequest,productFile));
    }

    @PutMapping
    public ResponseEntity<MessageResponse> updateProduct(@Valid @RequestPart("updateProductRequest") UpdateProductRequest updateProductRequest
            , @RequestPart("productFile")MultipartFile productFile) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iproductService.updateProduct(updateProductRequest,productFile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteProduct(@PathVariable Integer id) {
        this.iproductService.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("product với id = '" + id + "' đã được xóa thành công"));
    }

}
