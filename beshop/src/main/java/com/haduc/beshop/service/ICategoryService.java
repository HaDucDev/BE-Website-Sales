package com.haduc.beshop.service;

import com.haduc.beshop.model.Category;
import com.haduc.beshop.util.dto.request.admin.CreateCategoryRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateCategoryRequest;
import com.haduc.beshop.util.dto.response.admin.GetCategoryResponse;
import com.haduc.beshop.util.dto.response.account.MessageResponse;

import java.util.List;

public interface ICategoryService {

    //admin

    //* lay tat ca cate con co trong kho
    List<Category> getAllCategory();

    GetCategoryResponse findByCategoryIdAndIsDeleteFalse(Integer categoryId);

    MessageResponse createCategory(CreateCategoryRequest createCategoryRequest);

    MessageResponse updateCategory(UpdateCategoryRequest updateCategoryRequest);

    void deleteById(Integer id);

}
