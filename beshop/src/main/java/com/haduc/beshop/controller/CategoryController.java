package com.haduc.beshop.controller;

import com.haduc.beshop.model.Category;
import com.haduc.beshop.service.ICategoryService;
import com.haduc.beshop.util.dto.request.admin.CreateCategoryRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateCategoryRequest;
import com.haduc.beshop.util.dto.response.admin.GetCategoryResponse;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
    }


    @Secured({"ROLE_ADMIN"})
    @GetMapping("/admin/{id}")
    public ResponseEntity<GetCategoryResponse> getCategoryById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iCategoryService.findByCategoryIdAndIsDeleteFalse(id));
    }

    @Secured({"ROLE_ADMIN"})
    @PostMapping("/admin")
    public ResponseEntity<MessageResponse> createCategory(@RequestBody @Valid CreateCategoryRequest createCategoryRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iCategoryService.createCategory(createCategoryRequest));
    }

    @Secured({"ROLE_ADMIN"})
    @PutMapping("/admin")
    public ResponseEntity<MessageResponse> updateCategory(@RequestBody @Valid UpdateCategoryRequest updateCategoryRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(this.iCategoryService.updateCategory(updateCategoryRequest));
    }
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable Integer id) {
        this.iCategoryService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Category với id = '" + id + "' đã được xóa thành công"));
    }


}
