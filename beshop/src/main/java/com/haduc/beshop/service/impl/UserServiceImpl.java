package com.haduc.beshop.service.impl;

import com.haduc.beshop.config.sendEmail.SendMail;
import com.haduc.beshop.model.Role;
import com.haduc.beshop.model.User;
import com.haduc.beshop.repository.IRoleRepository;
import com.haduc.beshop.repository.IUserRepository;
import com.haduc.beshop.service.IUserService;
import com.haduc.beshop.util.FunctionCommon;
import com.haduc.beshop.util.dto.request.admin.CreateUserRequest;
import com.haduc.beshop.util.dto.request.admin.UpdateUserRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.dto.response.admin.GetUserResponse;
import com.haduc.beshop.util.dto.response.admin.GetUsersPaginationResponse;
import com.haduc.beshop.util.enum_role.ERole;
import com.haduc.beshop.util.exception.NotXException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private ModelMapper modelMapper;

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
        Page<User> userPage = this.iUserRepository.findAllByIsDeleteFalse(pageable);

        GetUsersPaginationResponse getUsersPaginationResponse = this.modelMapper.map(userPage, GetUsersPaginationResponse.class);// lay 4 thuoc duoi ko co content

        // convert tung phan tu trong list.
        getUsersPaginationResponse.setContent(
                userPage.getContent().stream().map(user -> this.modelMapper.map(user, GetUserResponse.class)).collect(Collectors.toList()));

        return getUsersPaginationResponse;
    }

    @Override
    public GetUserResponse findByUserIdAndIsDeleteFalse(Integer userId) {
        User user = this.iUserRepository.findByUserIdAndIsDeleteFalse(userId)
                .orElseThrow(() -> new NotXException("Không tìm thấy user này", HttpStatus.NOT_FOUND));
        GetUserResponse getUserResponse = this.modelMapper.map(user, GetUserResponse.class);
        getUserResponse.setRoleName(user.getRole().getName().toString());
        getUserResponse.setRoleId(user.getRole().getId());
        return getUserResponse;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public MessageResponse createUser(CreateUserRequest createUserRequest) {

        if (this.iUserRepository.findByEmailAndIsDeleteFalse(createUserRequest.getEmail()).isPresent()) {
            throw new NotXException("Email này đã tồn tại vui chọn tên khác", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        user.setPassword(passwordEncoder.encode(pass));//ma hoa roi luu
        User user1 = this.iUserRepository.save(user);
        //luu nguoi dung thanh cong ms gui mail
        this.sendMail.sendMailWithText("Đăng ký tài khoản", "Đây là password của bạn: " + pass + ". Hãy đổi mật khẩu sau khi đăng nhập nhé!", user1.getEmail());//user1.getEmail() la mail gui den
        return new MessageResponse(String.format("User %s được lưu thành công!", user1.getFullName()));

    }

    @Override
    public MessageResponse updateUser(UpdateUserRequest updateSupplierRequest) {

        Role role = this.iRoleRepository.findById(updateSupplierRequest.getRoleId()).orElseThrow(() -> new NotXException("Không tìm thấy role này", HttpStatus.NOT_FOUND));

        User user = this.iUserRepository.findByUserIdAndIsDeleteFalse(updateSupplierRequest.getUserId()).orElseThrow(() -> new NotXException("Không tìm thấy user này", HttpStatus.NOT_FOUND));
        user.setEmail(updateSupplierRequest.getEmail());
        user.setFullName(updateSupplierRequest.getFullName());
        //user.setUsername(updateSupplierRequest.getUsername());// dc duoc sua nhe
        user.setAddress(updateSupplierRequest.getAddress());
        user.setPhone(updateSupplierRequest.getPhone());
        user.setRole(role);
        User user1 = this.iUserRepository.save(user);
        return new MessageResponse(String.format("User %s được lưu thành công!", user1.getFullName()));
    }

    @Transactional
    @Override
    public void deleteById(Integer id) {
        int affectedRows = this.iUserRepository.softDeleteUser(id);
        System.out.println(affectedRows);
        if (affectedRows == 0) {
            throw new NotXException("Xảy ra lỗi khi xóa supplier", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
