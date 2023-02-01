package com.haduc.beshop.service.impl;

import com.haduc.beshop.model.Category;
import com.haduc.beshop.repository.ICategoryRepository;
import com.haduc.beshop.service.ICategoryService;
import com.haduc.beshop.util.payload.request.admin.CategoryResponse;
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
    public CategoryResponse findByCategoryIdAndIsDeleteFalse(Integer categoryId) {
        return modelMapper.map(this.iCategoryRepository.findByCategoryIdAndIsDeleteFalse(categoryId),CategoryResponse.class);
    }
}
