package com.Travel.service.impl;

import com.Travel.config.JwtProvider;
import com.Travel.domain.USER_ROLE;
import com.Travel.model.Seller;
import com.Travel.model.User;
import com.Travel.model.VerificationCode;
import com.Travel.repository.SellerRepository;
import com.Travel.repository.UserRepository;
import com.Travel.repository.VerificationRepository;
import com.Travel.request.LoginRequest;
import com.Travel.request.SignupRequest;
import com.Travel.response.AuthResponse;
import com.Travel.service.AuthService;
import com.Travel.service.EmailService;
import com.Travel.utils.OtpUtil;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;
    private final VerificationRepository verificationRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    @Override
    public void sentLoginOtp(String email, USER_ROLE role) throws Exception {
        if(role.equals(USER_ROLE.ROLE_SELLER)){
            Seller seller = sellerRepository.findByEmail(email);
            if(seller == null) throw new Exception("Seller email not found!");
        } else {
            User user = userRepository.findByEmail(email);
            if(user == null) throw new Exception("User email not found!");
        }

        // Xóa OTP cũ nếu tồn tại
        VerificationCode isExists = verificationRepository.findByEmail(email);
        if(isExists != null){
            verificationRepository.delete(isExists);
        }

        // Tạo OTP mới
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setOtp(otp);
        verificationRepository.save(verificationCode);

        // Gửi email
        String subject = "Welcome To Travel Ecommerce";
        String text = "Your OTP: " + otp;
        emailService.sendVerificationOtpMail(email, otp, subject, text);
    }

    @Override
    public String createUser(SignupRequest req) throws Exception {
        // Kiểm tra OTP
        VerificationCode verificationCode = verificationRepository.findByEmail(req.getEmail());
        if(verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())){
            throw new Exception("Wrong OTP");
        }

        // Kiểm tra user đã tồn tại chưa
        User user = userRepository.findByEmail(req.getEmail());
        if(user == null){
            User newUser = new User();
            newUser.setEmail(req.getEmail());
            newUser.setFullName(req.getFullName());
            newUser.setMobile(req.getMobile());
            newUser.setRole(USER_ROLE.ROLE_CUSTOMER); // Hoặc lấy từ req nếu muốn
            newUser.setPassword(passwordEncoder.encode(req.getOtp()));
            user = userRepository.save(newUser);
        }

        // Tạo Authentication và JWT
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signing(LoginRequest req) throws Exception {
        String email = req.getEmail();
        String otp = req.getOtp();

        // Check OTP
        VerificationCode verificationCode = verificationRepository.findByEmail(email);
        if(verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("Invalid OTP");
        }

        // Tìm user hoặc seller
        User user = userRepository.findByEmail(email);
        USER_ROLE role;
        if(user != null) {
            role = user.getRole();
        } else {
            Seller seller = sellerRepository.findByEmail(email);
            if(seller != null) {
                role = seller.getRole();
            } else {
                throw new Exception("User/Seller not found");
            }
        }

        // Build Authentication
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Tạo JWT
        String token = jwtProvider.generateToken(authentication);

        return new AuthResponse(token, "Login successful", role);
    }

    @Override
    public void sentSignOtp(String email) {
        VerificationCode oldOtp = verificationRepository.findByEmail(email);
        if(oldOtp!=null){
            verificationRepository.delete(oldOtp);
        }
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setCreatAt(LocalDateTime.now());
        verificationCode.setOtp(otp);
        verificationRepository.save(verificationCode);
        String subject = "Welcome to Travel Ecommerce - Signup OTP";
        String text = "Your signup OTP is: " + otp;
        emailService.sendVerificationOtpMail(email, otp, subject, text);
    }
}
