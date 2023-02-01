package com.haduc.beshop.service;

import com.haduc.beshop.model.Category;

import java.util.List;

public interface ICategoryService {

    //admin

    //* lay tat ca san pham con co trong kho
    List<Category> getAllCategory();

}
