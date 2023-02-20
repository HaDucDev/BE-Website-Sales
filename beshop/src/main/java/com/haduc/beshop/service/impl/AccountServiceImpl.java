package com.haduc.beshop.service.impl;

import com.haduc.beshop.config.jwt.JwtUtils;
import com.haduc.beshop.config.springSecurity.MyUserDetailsService;
import com.haduc.beshop.model.User;
import com.haduc.beshop.repository.IUserRepository;
import com.haduc.beshop.service.IAccountService;
import com.haduc.beshop.util.dto.request.account.LoginRequest;
import com.haduc.beshop.util.dto.response.account.LoginResponse;
import com.haduc.beshop.util.exception.NotXException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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

        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        }catch (BadCredentialsException e) {
            throw new NotXException("Tên đăng nhập hoặc mật khẩu không đúng", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //User user = this.iUserRepository.findByUsername(request.getUsername()).orElseThrow(() -> new NotXException("không tìm thấy người dùng này", HttpStatus.NOT_FOUND));
        final UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = this.jwtUtils.generateToken(userDetails);// sinh token
        User user = this.iUserRepository.findByUsername(request.getUsername()).orElseThrow(() -> new NotXException("không tìm thấy người dùng này", HttpStatus.NOT_FOUND));

        return new LoginResponse(jwt, user.getUserId(), user.getUsername(),
                user.getRole().getName().name(), user.getRole().getId(),user.getAvatar());
    }
}
