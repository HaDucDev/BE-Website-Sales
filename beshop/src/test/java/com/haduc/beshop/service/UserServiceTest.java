package com.haduc.beshop.service;

import com.haduc.beshop.config.sendEmail.SendMail;
import com.haduc.beshop.model.Role;
import com.haduc.beshop.model.User;
import com.haduc.beshop.repository.IRoleRepository;
import com.haduc.beshop.repository.IUserRepository;
import com.haduc.beshop.util.dto.request.account.RegisterRequest;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.enum_role.ERole;
import com.haduc.beshop.util.exception.NotXException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    private IUserRepository iUserRepository;

    @MockBean
    private IRoleRepository iRoleRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private SendMail sendMail;

    @Autowired
    private IAccountService iAccountService;

    @Test
    public void testRegisterSuccess() {
        // Setup <+> init data test controller
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setFullName("Test User");
        registerRequest.setUsername("testuser");
        registerRequest.setAddress("Test Address");
        registerRequest.setPhone("123456789");

        User user = new User();
        user.setEmail("test@example.com");
        user.setFullName("Test User");

        when(iUserRepository.findByEmailAndIsDeleteFalse(anyString())).thenReturn(Optional.empty());
        when(iRoleRepository.findByName(any())).thenReturn(Optional.of(new Role()));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(iUserRepository.save(any(User.class))).thenReturn(user);

        // Act
        MessageResponse response = iAccountService.register(registerRequest);

        // Verify
        verify(iUserRepository).findByEmailAndIsDeleteFalse(registerRequest.getEmail());
        verify(iRoleRepository).findByName(ERole.ROLE_CUSTOMER);
        verify(iUserRepository).save(any(User.class));
        verify(sendMail).sendMailWithText(anyString(), anyString(), anyString());
        // verify() giúp đảm bảo rằng các phương thức đã được gọi đúng cách
        // trong quá trình kiểm thử, từ đó xác nhận rằng các hành vi mong muốn đã xảy ra.
        assertEquals(String.format("User %s đã tạo tài khoản thành công!", registerRequest.getFullName()), response.getMessage());
    }

    @Test
    public void testRegisterNoEmailSentIfUserExists() {
        // Setup
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("existing@example.com");

        User existingUser = new User();
        existingUser.setEmail("existing@example.com");

        // Giả lập hành vi: Email đã tồn tại trong hệ thống
        when(iUserRepository.findByEmailAndIsDeleteFalse(registerRequest.getEmail())).thenReturn(Optional.of(existingUser));

        // Act
        try {
            iAccountService.register(registerRequest);
            fail("Should have thrown an exception");
        } catch (NotXException e) {
            // Exception expected, do nothing
        }

        // Verify
        verify(iUserRepository).findByEmailAndIsDeleteFalse(registerRequest.getEmail());

        // Kiểm tra rằng phương thức sendMail không được gọi vì tài khoản đã tồn tại
        verify(sendMail, never()).sendMailWithText(anyString(), anyString(), anyString());
    }
}
