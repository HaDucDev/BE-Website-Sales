package com.haduc.beshop.controller;

import com.haduc.beshop.model.Product;
import com.haduc.beshop.service.IproductService;
import com.haduc.beshop.util.dto.request.admin.CreateProductRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateProductRequest;
import com.haduc.beshop.util.dto.response.admin.GetProductDetailResponse;
import com.haduc.beshop.util.dto.response.admin.MessageResponse;
import com.haduc.beshop.util.dto.response.user.GetProductsPaginationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)// phep các bat ki api nao goi
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private IproductService iproductService;

    //admin
    @GetMapping("/admin")
    public ResponseEntity<List<Product>> getAllProduct() {
        return  ResponseEntity.status(HttpStatus.OK).body(this.iproductService.getAllProduct());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetProductDetailResponse> getProductById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iproductService.findByProductIdAndIsDeleteFalse(id));
    }

    @PostMapping("/admin")
    public ResponseEntity<MessageResponse> createProduct(@Valid @RequestPart("createProductRequest") CreateProductRequest createProductRequest
            , @RequestPart(value = "productFile",required = false)MultipartFile productFile) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iproductService.createProduct(createProductRequest,productFile));
    }

    @PutMapping("/admin")
    public ResponseEntity<MessageResponse> updateProduct(@Valid @RequestPart("updateProductRequest") UpdateProductRequest updateProductRequest
            , @RequestPart(value = "productFile",required = false)MultipartFile productFile) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iproductService.updateProduct(updateProductRequest,productFile));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<MessageResponse> deleteProduct(@PathVariable Integer id) {
        this.iproductService.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("product với id = '" + id + "' đã được xóa thành công"));
    }

    // ================================user
    @GetMapping
    public ResponseEntity<GetProductsPaginationResponse> getAllHomeProduct
    (@RequestParam(defaultValue = "0") int number, @RequestParam(defaultValue = "6") int size, @PageableDefault(sort = "userId") Sort sort){
        Pageable paging = PageRequest.of(number, size,sort);
        return ResponseEntity.ok(this.iproductService.getAllProductAndIsDeleteFalsePagination(paging));
    }


}
