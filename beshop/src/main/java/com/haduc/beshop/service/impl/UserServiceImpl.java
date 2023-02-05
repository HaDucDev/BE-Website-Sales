package com.haduc.beshop.service.impl;

import com.haduc.beshop.model.User;
import com.haduc.beshop.repository.IUserRepository;
import com.haduc.beshop.service.IUserService;
import com.haduc.beshop.util.dto.response.admin.GetUserResponse;
import com.haduc.beshop.util.exception.NotXException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private ModelMapper  modelMapper;

    @Override
    public List<User> getAllUser() {
        return this.iUserRepository.findAllByIsDeleteFalse();
    }

    @Override
    public GetUserResponse findByUserIdAndIsDeleteFalse(Integer userId) {
        User user=this.iUserRepository.findByUserIdAndIsDeleteFalse(userId)
                .orElseThrow(()->new NotXException("Không tìm thấy user này", HttpStatus.NOT_FOUND));
        GetUserResponse getUserResponse= this.modelMapper.map(user,GetUserResponse.class);
        getUserResponse.setRoleName(user.getRole().getName().toString());
        return getUserResponse;
    }
}
