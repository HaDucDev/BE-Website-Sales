package com.haduc.beshop.service;

import com.haduc.beshop.model.Category;
import com.haduc.beshop.util.payload.request.admin.CategoryRequest;
import com.haduc.beshop.util.payload.response.admin.GetCategoryResponse;
import com.haduc.beshop.util.payload.response.admin.MessageResponse;

import java.util.List;

public interface ICategoryService {

    //admin

    //* lay tat ca san pham con co trong kho
    List<Category> getAllCategory();

    GetCategoryResponse findByCategoryIdAndIsDeleteFalse(Integer categoryId);

    MessageResponse createCategory(CategoryRequest categoryRequest );

}
