package com.haduc.beshop.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.haduc.beshop.config.jwt.JwtUtils;
import com.haduc.beshop.config.sendEmail.SendMail;
import com.haduc.beshop.config.springSecurity.MyUserDetailsService;
import com.haduc.beshop.model.User;
import com.haduc.beshop.repository.IRoleRepository;
import com.haduc.beshop.repository.IUserRepository;
import com.haduc.beshop.service.IAccountService;
import com.haduc.beshop.util.FunctionCommon;
import com.haduc.beshop.util.dto.request.account.*;
import com.haduc.beshop.util.dto.response.account.LoginResponse;
import com.haduc.beshop.util.dto.response.account.MessageResponse;
import com.haduc.beshop.util.enum_role.ERole;
import com.haduc.beshop.util.exception.NotXException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

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
    private Date dateExpiration;

    @Override
    public LoginResponse login(LoginRequest request) {

        try {//xac thuc
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            throw new NotXException("Tên đăng nhập hoặc mật khẩu không đúng", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        final UserDetails userDetails = this.myUserDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = this.jwtUtils.generateToken(userDetails);// sinh token
        User user = this.iUserRepository.findByUsernameAndIsDeleteFalse(request.getUsername()).orElseThrow(() -> new NotXException("không tìm thấy người dùng này", HttpStatus.NOT_FOUND));

        return new LoginResponse(jwt, user.getUserId(), user.getUsername(),
                user.getRole().getName().name(), user.getRole().getId(), user.getAvatar());
    }

    @Autowired
    private IRoleRepository iRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SendMail sendMail;

    @Override
    public MessageResponse register(RegisterRequest registerRequest) {

        if (this.iUserRepository.findByEmailAndIsDeleteFalse(registerRequest.getEmail()).isPresent()) {
            throw new NotXException("Email này đã tồn tại vui chọn tên khác", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
        User user1 = this.iUserRepository.save(user);
        //luu nguoi dung thanh cong ms gui mail
        this.sendMail.sendMailWithText("Đăng ký tài khoản", "Chào mừng quý khách đến với HDSHOP Đây là password của bạn: " + pass + ". Hãy đổi mật khẩu sau khi đăng nhập nhé!", user1.getEmail());//user1.getEmail() la mail gui den
        return new MessageResponse(String.format("User %s đã tạo tài khoản thành công!", user1.getFullName()));
    }

    @Override
    public MessageResponse changePass(ChangePasswordRequest changePasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = this.iUserRepository.findByUsernameAndIsDeleteFalse(username).orElseThrow(() -> new NotXException("Lỗi bạn chưa đăng nhập", HttpStatus.NOT_FOUND));
        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {// ham san kiem tra trung pass ko
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            this.iUserRepository.save(user);
            return new MessageResponse(String.format("User %s đã tạo đổi mật khẩu thành công!", user.getFullName()));
        }
        throw new NotXException("Mật khẩu cũ không chính xác. Nếu bạn quên hãy đăng xuất và chọn chức năng quên mật khẩu", HttpStatus.NOT_FOUND);
    }

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public MessageResponse updateInforUser(ChangeInforAccountRequest changeInforAccountRequest, MultipartFile avatar) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = this.iUserRepository.findByUsernameAndIsDeleteFalse(username).orElseThrow(() -> new NotXException("Lỗi bạn chưa đăng nhập", HttpStatus.NOT_FOUND));

        if (user.getEmail().equals(changeInforAccountRequest.getEmail()) == false) {
            user.setEmail(changeInforAccountRequest.getEmail());
        }
        user.setFullName(changeInforAccountRequest.getFullName());
        user.setAddress(changeInforAccountRequest.getAddress());
        user.setPhone(changeInforAccountRequest.getPhone());

        if (avatar == null || avatar.isEmpty() == true) {
            user.setAvatar(user.getAvatar());
        } else {

            try {
                Map p = this.cloudinary.uploader().upload(avatar.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                String image = (String) p.get("secure_url");
                user.setAvatar(image);
            } catch (IOException e) {
                System.out.println("loi post update product" + e.getMessage());
            }
        }

        User userSave = this.iUserRepository.save(user);
        return new MessageResponse(String.format("User %s được sửa thành công!", userSave.getFullName()));
    }

    @Override
    public MessageResponse createtTokenCodeWhenFortgetPass(ForgetPasswordRequest forgetPasswordRequest) {
        User user = this.iUserRepository.findByEmailAndIsDeleteFalse(forgetPasswordRequest.getEmail()).orElseThrow(() -> new NotXException("Không tìm thấy email này", HttpStatus.NOT_FOUND));
        //tao token xac thuc
        String tokenCode = FunctionCommon.getRandomNumber(9);
        user.setTokenResetPass(tokenCode);
        // tao thoi gian het han token
//        Date currentDate = new Date(System.currentTimeMillis());
//        Date expirationTime = new Date(currentDate.getTime() + 10 * 60 * 1000); // thêm 10 phút
        Date expirationTime = new Date(System.currentTimeMillis() + 10 * 60 * 1000); // thêm 10 phút
        user.setExpirationTimeToken(expirationTime);
        User user1 = this.iUserRepository.save(user);
        this.sendMail.sendMailWithText("Quên mật khẩu", "Chào mừng quý khách đến với HDSHOP. Đây là mã xác nhận của bạn của bạn: " + tokenCode + ". Sau 10 phút sẽ hết hạn", user1.getEmail());//user1.getEmail() la mail gui den
        return new MessageResponse(String.format("Hãy kiểm tra email %s để lấy mã xác nhận!", user1.getEmail()));

    }

    // gui mat khau moi ve email neu ma xac nhan hop le
    @Override
    public MessageResponse setPasswordRandomAndSendNewPassMail(SetPasswordRandomRequest setPasswordRandomRequest) {
        User user = this.iUserRepository.findByEmailAndIsDeleteFalse(setPasswordRandomRequest.getEmail()).orElseThrow(() -> new NotXException("Không tìm thấy email này", HttpStatus.NOT_FOUND));

        // lay thoi gian thuc hien gui ma
        Date currentDate = new Date(System.currentTimeMillis());
        //lay thoi gian trong csdl len de kiem tra
        Date dateExpiration = user.getExpirationTimeToken();
        //kiem tra token co dung va con han hay khong
        if (setPasswordRandomRequest.getResetCode().equals(user.getTokenResetPass())) {
            if (currentDate.compareTo(dateExpiration) < 0) {// kiem tra time da het han chua
                // tao mat khau ngua nhien
                String pass = FunctionCommon.getRandomNumber(8) + "ok";
                user.setPassword(passwordEncoder.encode(pass));//ma hoa roi luu
                User user1 = this.iUserRepository.save(user);
                this.sendMail.sendMailWithText("Mật khẩu mới", "Chào mừng quý khách đến với HDSHOP Đây là password mới của bạn: " + pass + ". Hãy đổi mật khẩu sau khi đăng nhập nhé!", user1.getEmail());
                return new MessageResponse(String.format("Email %s đã xác nhận đổi mật khẩu thành công! Hãy vào email dê lấy mật khẩu mới", user1.getEmail()));
            }

            throw new NotXException("Mã xác đã hết hạn", HttpStatus.UNAUTHORIZED);
        }
        throw new NotXException("Mã xác nhận lỗi", HttpStatus.BAD_REQUEST);
    }
}
