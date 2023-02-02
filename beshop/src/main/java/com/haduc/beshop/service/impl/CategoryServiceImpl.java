package com.haduc.beshop.service.impl;

import com.haduc.beshop.model.Category;
import com.haduc.beshop.repository.ICategoryRepository;
import com.haduc.beshop.service.ICategoryService;
import com.haduc.beshop.util.payload.request.admin.CategoryRequest;
import com.haduc.beshop.util.payload.response.admin.GetCategoryResponse;
import com.haduc.beshop.util.payload.response.admin.MessageResponse;
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
    public MessageResponse createCategory(CategoryRequest categoryRequest) {

        Category category= new Category();
        category.setCategoryName(categoryRequest.getCategoryName());
        //this.iCategoryRepository.save(category); luu
        Category savaCategory= this.iCategoryRepository.save(category);
        return new MessageResponse(String.format("Category %s được tạo thành công!", savaCategory.getCategoryName()));

    }
}
