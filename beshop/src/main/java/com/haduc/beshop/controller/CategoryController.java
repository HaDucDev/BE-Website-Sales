package com.haduc.beshop.controller;

import com.haduc.beshop.model.Category;
import com.haduc.beshop.service.ICategoryService;
import com.haduc.beshop.util.dto.request.admin.CreateCategoryRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateCategoryRequest;
import com.haduc.beshop.util.dto.response.admin.GetCategoryResponse;
import com.haduc.beshop.util.dto.response.admin.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin/category")
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
    public ResponseEntity<MessageResponse> updateCategory(@RequestBody @Valid UpdateCategoryRequest updateCategoryRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iCategoryService.updateCategory(updateCategoryRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable Integer id) {
        this.iCategoryService.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Category với id = '" + id + "' đã được xóa thành công"));
    }


}
