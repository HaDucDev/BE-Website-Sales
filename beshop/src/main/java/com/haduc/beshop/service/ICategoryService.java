package com.haduc.beshop.service;

import com.haduc.beshop.model.Category;
import com.haduc.beshop.util.payload.request.admin.CreateCategoryRequest;
import com.haduc.beshop.util.payload.request.admin.UpdateCategoryRequest;
import com.haduc.beshop.util.payload.response.admin.GetCategoryResponse;
import com.haduc.beshop.util.payload.response.admin.MessageResponse;
import com.haduc.beshop.util.payload.response.admin.UpdateCategoryResponse;

import java.util.List;

public interface ICategoryService {

    //admin

    //* lay tat ca san pham con co trong kho
    List<Category> getAllCategory();

    GetCategoryResponse findByCategoryIdAndIsDeleteFalse(Integer categoryId);

    MessageResponse createCategory(CreateCategoryRequest createCategoryRequest);

    UpdateCategoryResponse updateCategory(UpdateCategoryRequest updateCategoryRequest);

}
