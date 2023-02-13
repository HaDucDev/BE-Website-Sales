package com.haduc.beshop.service.impl;


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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements IAccountService {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IUserRepository iUserRepository;


    @Override
    public LoginResponse login(LoginRequest request)  {

        System.out.println("ok1");
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()
            ));
        }catch (BadCredentialsException e) {
            throw new NotXException("Tên đăng nhập hoặc mật khẩu không đúng", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        User user = this.iUserRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy username: "));
        System.out.println("ok1");
        return new LoginResponse( user.getUserId(), user.getUsername(),
                user.getRole().getName().name(), user.getRole().getId());
    }
}
