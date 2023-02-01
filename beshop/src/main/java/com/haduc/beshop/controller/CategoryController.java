package com.haduc.beshop.controller;

import com.haduc.beshop.model.Category;
import com.haduc.beshop.service.ICategoryService;
import com.haduc.beshop.util.payload.request.admin.CategoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iCategoryService.findByCategoryIdAndIsDeleteFalse(id));
    }





}
