package com.haduc.beshop.service;

import com.haduc.beshop.model.Category;
import com.haduc.beshop.util.payload.request.admin.CategoryResponse;

import java.util.List;

public interface ICategoryService {

    //admin

    //* lay tat ca san pham con co trong kho
    List<Category> getAllCategory();

    CategoryResponse findByCategoryIdAndIsDeleteFalse(Integer categoryId);

}
