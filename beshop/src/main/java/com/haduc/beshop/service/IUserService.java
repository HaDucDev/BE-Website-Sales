package com.haduc.beshop.service;

import com.haduc.beshop.model.User;
import com.haduc.beshop.util.dto.request.admin.CreateUserRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateUserRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.dto.response.admin.GetUserResponse;
import com.haduc.beshop.util.dto.response.admin.GetUsersPaginationResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {

    //admin
    //* lay tat ca user
    List<User> getAllUser();

    GetUserResponse findByUserIdAndIsDeleteFalse(Integer userId);

    GetUsersPaginationResponse getAllUserAndIsDeleteFalsePagination(Pageable pageable);


    MessageResponse createUser(CreateUserRequest createUserRequest);

    MessageResponse updateUser(UpdateUserRequest updateSupplierRequest);

    void deleteById(Integer id);
}
