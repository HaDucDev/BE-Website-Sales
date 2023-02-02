package com.haduc.beshop.controller;

import com.haduc.beshop.model.Category;
import com.haduc.beshop.service.ICategoryService;
import com.haduc.beshop.util.payload.request.admin.CreateCategoryRequest;
import com.haduc.beshop.util.payload.request.admin.UpdateCategoryRequest;
import com.haduc.beshop.util.payload.response.admin.GetCategoryResponse;
import com.haduc.beshop.util.payload.response.admin.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/category")
public class CategoryController {


    @Autowired
    private ICategoryService iCategoryService;


    @GetMapping
    public ResponseEntity<List<Category>> getAllCategory() {
        return  ResponseEntity.status(HttpStatus.OK).body(this.iCategoryService.getAllCategory());
        //return ResponseEntity.ok(this.iCategoryService.getAllCategory());
    }


    @GetMapping("/{id}")
    public ResponseEntity<GetCategoryResponse> getCategoryById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iCategoryService.findByCategoryIdAndIsDeleteFalse(id));
    }


    @PostMapping
    public ResponseEntity<MessageResponse> createCategory(@RequestBody @Valid CreateCategoryRequest createCategoryRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iCategoryService.createCategory(createCategoryRequest));
    }


    @PutMapping
    public ResponseEntity<MessageResponse> updateCategory(@RequestBody @Valid UpdateCategoryRequest createCategoryRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                        .body(new MessageResponse(
                        String.format("Loại hàng có id là %s được cập nhật thành công!",
                                this.iCategoryService.updateCategory(createCategoryRequest).getCategoryId().toString())
                ));
    }


}
