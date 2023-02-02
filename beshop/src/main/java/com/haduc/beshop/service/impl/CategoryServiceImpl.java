package com.haduc.beshop.service.impl;

import com.haduc.beshop.model.Category;
import com.haduc.beshop.repository.ICategoryRepository;
import com.haduc.beshop.service.ICategoryService;
import com.haduc.beshop.util.payload.request.admin.CreateCategoryRequest;
import com.haduc.beshop.util.payload.request.admin.UpdateCategoryRequest;
import com.haduc.beshop.util.payload.response.admin.GetCategoryResponse;
import com.haduc.beshop.util.payload.response.admin.MessageResponse;
import com.haduc.beshop.util.payload.response.admin.UpdateCategoryResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private ICategoryRepository iCategoryRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<Category> getAllCategory() {
        return this.iCategoryRepository.findAllByIsDeleteFalse();
    }

    @Override
    public GetCategoryResponse findByCategoryIdAndIsDeleteFalse(Integer categoryId) {
        return this.modelMapper.map(this.iCategoryRepository.findByCategoryIdAndIsDeleteFalse(categoryId), GetCategoryResponse.class);
    }

    @Override
    public MessageResponse createCategory(CreateCategoryRequest createCategoryRequest) {

        Category category= new Category();
        category.setCategoryName(createCategoryRequest.getCategoryName());
        //this.iCategoryRepository.save(category); luu
        Category savaCategory= this.iCategoryRepository.save(category);
        return new MessageResponse(String.format("Category %s được tạo thành công!", savaCategory.getCategoryName()));

    }

    @Override
    public UpdateCategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        Category category = this.iCategoryRepository.findByCategoryIdAndIsDeleteFalse(updateCategoryRequest.getCategoryId());
        category.setCategoryName(updateCategoryRequest.getCategoryName());
        Category savaCategory= this.iCategoryRepository.save(category);
        UpdateCategoryResponse updateCategory= this.modelMapper.map(savaCategory,UpdateCategoryResponse.class);
        return updateCategory;
    }
}
