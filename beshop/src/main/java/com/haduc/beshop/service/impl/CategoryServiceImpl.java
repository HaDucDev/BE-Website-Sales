package com.haduc.beshop.service.impl;

import com.haduc.beshop.model.Category;
import com.haduc.beshop.repository.ICategoryRepository;
import com.haduc.beshop.service.ICategoryService;
import com.haduc.beshop.util.exception.NotXException;
import com.haduc.beshop.util.dto.request.admin.CreateCategoryRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateCategoryRequest;
import com.haduc.beshop.util.dto.response.admin.GetCategoryResponse;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public  MessageResponse updateCategory(UpdateCategoryRequest updateCategoryRequest) {
        Category category =  this.iCategoryRepository
                .findByCategoryIdAndIsDeleteFalse(updateCategoryRequest.getCategoryId())
                .orElseThrow(() -> new NotXException("Không tìm thấy category này",HttpStatus.NOT_FOUND));
        category.setCategoryName(updateCategoryRequest.getCategoryName());
        Category savaCategory= this.iCategoryRepository.save(category);
        return new MessageResponse(String.format("Loại hàng có id là %s được cập nhật thành công!", savaCategory.getCategoryId().toString()));
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {

        int affectedRows = this.iCategoryRepository.softDeleteCategory(id);
        System.out.println(affectedRows);
        if (affectedRows == 0) {
            throw new NotXException("Xảy ra lỗi khi xóa category", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
