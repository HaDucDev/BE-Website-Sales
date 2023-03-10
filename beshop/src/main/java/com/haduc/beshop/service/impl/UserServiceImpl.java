package com.haduc.beshop.service.impl;

import com.haduc.beshop.config.sendEmail.SendMail;
import com.haduc.beshop.model.User;
import com.haduc.beshop.repository.IRoleRepository;
import com.haduc.beshop.repository.IUserRepository;
import com.haduc.beshop.service.IUserService;
import com.haduc.beshop.util.FunctionCommon;
import com.haduc.beshop.util.dto.request.admin.CreateUserRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.dto.response.admin.GetUserResponse;
import com.haduc.beshop.util.dto.response.admin.GetUsersPaginationResponse;
import com.haduc.beshop.util.exception.NotXException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private ModelMapper  modelMapper;

    @Autowired
    private IRoleRepository iRoleRepository;

    @Autowired
    private SendMail sendMail;

    @Override
    public List<User> getAllUser() {
        return this.iUserRepository.findAllByIsDeleteFalse();
    }

    @Override
    public GetUsersPaginationResponse getAllUserAndIsDeleteFalsePagination(Pageable pageable) {
        Page<User> userPage= this.iUserRepository.findAllByIsDeleteFalse(pageable);

        GetUsersPaginationResponse getUsersPaginationResponse = this.modelMapper.map(userPage,GetUsersPaginationResponse.class);// lay 4 thuoc duoi ko co content

        // convert tung phan tu trong list.
        getUsersPaginationResponse.setContent(
                userPage.getContent().stream().map(user -> this.modelMapper.map(user,GetUserResponse.class)).collect(Collectors.toList()));

        return getUsersPaginationResponse;
    }

    @Override
    public GetUserResponse findByUserIdAndIsDeleteFalse(Integer userId) {
        User user=this.iUserRepository.findByUserIdAndIsDeleteFalse(userId)
                .orElseThrow(()->new NotXException("Không tìm thấy user này", HttpStatus.NOT_FOUND));
        GetUserResponse getUserResponse= this.modelMapper.map(user,GetUserResponse.class);
        getUserResponse.setRoleName(user.getRole().getName().toString());
        return getUserResponse;
    }

    @Override
    public MessageResponse createUser(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setRole(this.iRoleRepository.findById(createUserRequest.getRoleId()).orElseThrow(() -> new NotXException("Không tìm thấy role này", HttpStatus.NOT_FOUND)));
        user.setAvatar("https://res.cloudinary.com/dkdyl2pcy/image/upload/v1676872862/avatar-default-9_rv6k1c.png");// image mac dinh
        user.setEmail(createUserRequest.getEmail());
        user.setFullName(createUserRequest.getFullName());
        user.setUsername(createUserRequest.getUsername());
        user.setAddress(createUserRequest.getAddress());
        user.setPhone(createUserRequest.getPhone());

        //tao mat khau ngau nhien
        String pass = FunctionCommon.getRandomNumber(8);
        user.setPassword(pass);
        User user1= this.iUserRepository.save(user);
        //luu nguoi dung thanh cong ms gui mail
        this.sendMail.sendMailWithText("Đăng ký tài khoản", "Đây là password của bạn: " + user1.getPassword(), user1.getEmail());
        return new MessageResponse(String.format("User %s được sửa thành công!", user1.getFullName()));
    }


}
