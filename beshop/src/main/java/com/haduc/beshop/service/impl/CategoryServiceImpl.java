package com.haduc.beshop.service.impl;

import com.haduc.beshop.model.Category;
import com.haduc.beshop.repository.ICategoryRepository;
import com.haduc.beshop.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private ICategoryRepository iCategoryRepository;

    @Override
    public List<Category> getAllCategory() {
        return this.iCategoryRepository.findAllByIsDeleteFalse();
    }
}
