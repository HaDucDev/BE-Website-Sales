package com.haduc.beshop.service.impl;

import com.haduc.beshop.config.jwt.JwtUtils;
import com.haduc.beshop.config.sendEmail.SendMail;
import com.haduc.beshop.config.springSecurity.MyUserDetailsService;
import com.haduc.beshop.model.User;
import com.haduc.beshop.repository.IRoleRepository;
import com.haduc.beshop.repository.IUserRepository;
import com.haduc.beshop.service.IAccountService;
import com.haduc.beshop.util.FunctionCommon;
import com.haduc.beshop.util.dto.request.account.LoginRequest;
import com.haduc.beshop.util.dto.request.account.RegisterRequest;
import com.haduc.beshop.util.dto.response.account.LoginResponse;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.enum_role.ERole;
import com.haduc.beshop.util.exception.NotXException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements IAccountService {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserRepository iUserRepository;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public LoginResponse login(LoginRequest request)  {

        try {//xac thuc
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        }catch (BadCredentialsException e) {
            throw new NotXException("Tên đăng nhập hoặc mật khẩu không đúng", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        final UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = this.jwtUtils.generateToken(userDetails);// sinh token
        User user = this.iUserRepository.findByUsernameAndIsDeleteFalse(request.getUsername()).orElseThrow(() -> new NotXException("không tìm thấy người dùng này", HttpStatus.NOT_FOUND));

        return new LoginResponse(jwt, user.getUserId(), user.getUsername(),
                user.getRole().getName().name(), user.getRole().getId(),user.getAvatar());
    }

    @Autowired
    private IRoleRepository iRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SendMail sendMail;

    @Override
    public MessageResponse register(RegisterRequest registerRequest) {
        User user = new User();
        user.setRole(this.iRoleRepository.findByName(ERole.ROLE_CUSTOMER).orElseThrow(() -> new NotXException("Không tìm thấy role này", HttpStatus.NOT_FOUND)));
        user.setAvatar("https://res.cloudinary.com/dkdyl2pcy/image/upload/v1676872862/avatar-default-9_rv6k1c.png");// image mac dinh
        user.setEmail(registerRequest.getEmail());
        user.setFullName(registerRequest.getFullName());
        user.setUsername(registerRequest.getUsername());
        user.setAddress(registerRequest.getAddress());
        user.setPhone(registerRequest.getPhone());
        //tao mat khau ngau nhien
        String pass = FunctionCommon.getRandomNumber(8);
        user.setPassword(passwordEncoder.encode(pass));//ma hoa roi luu
        User user1= this.iUserRepository.save(user);
        //luu nguoi dung thanh cong ms gui mail
        this.sendMail.sendMailWithText("Đăng ký tài khoản", "Đây là password của bạn: " + pass +". Hãy đổi mật khẩu sau khi đăng nhập nhé!", user1.getEmail());//user1.getEmail() la mail gui den
        return new MessageResponse(String.format("User %s được lưu thành công!", user1.getFullName()));
    }
}
