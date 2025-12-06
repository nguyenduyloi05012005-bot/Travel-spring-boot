package com.Travel.service.impl;

import com.Travel.config.JwtProvider;
import com.Travel.domain.USER_ROLE;
import com.Travel.exceptions.SellerException;
import com.Travel.exceptions.UserException;
import com.Travel.model.Seller;
import com.Travel.model.User;
import com.Travel.model.VerificationCode;
import com.Travel.repository.SellerRepository;
import com.Travel.repository.UserRepository;
import com.Travel.repository.VerificationRepository;
import com.Travel.request.LoginRequest;
import com.Travel.request.SellerSignupRequest;
import com.Travel.request.UserSignupRequest;
import com.Travel.response.AuthResponse;
import com.Travel.service.AuthService;
import com.Travel.service.EmailService;
import com.Travel.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public void sentLoginOtp(String email) throws Exception {
        USER_ROLE role;

        if (sellerRepository.findByEmail(email) != null) {
            role = USER_ROLE.ROLE_SELLER;
        } else if (userRepository.findByEmail(email) != null) {
            role = USER_ROLE.ROLE_CUSTOMER;
        } else {
            throw new UserException("Email không tồn tại!");
        }

        // Xóa OTP cũ
        VerificationCode old = verificationRepository.findByEmail(email);
        if (old != null) verificationRepository.delete(old);

        // Tạo OTP mới
        String otp = OtpUtil.generateOtp();

        VerificationCode vc = new VerificationCode();
        vc.setEmail(email);
        vc.setOtp(otp);
        verificationRepository.save(vc);

        // Gửi email
        emailService.sendVerificationOtpMail(
                email,
                otp,
                "Welcome To Travel Ecommerce",
                "Your OTP: " + otp
        );
    }



    @Override
    public AuthResponse signing(LoginRequest req) throws Exception {
        String email = req.getEmail();
        String otp = req.getOtp();

        // 1️⃣ Check OTP
        VerificationCode verificationCode = verificationRepository.findByEmail(email);
        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new UserException("Invalid OTP");
        }

        // 2️⃣ Tìm user hoặc seller
        USER_ROLE role;
        String principalEmail;

        User user = userRepository.findByEmail(email);
        if (user != null) {
            role = user.getRole();
            principalEmail = user.getEmail();
        } else {
            Seller seller = sellerRepository.findByEmail(email);
            if (seller != null) {
                role = seller.getRole();
                principalEmail = seller.getEmail();
            } else {
                throw new UserException("User/Seller not found");
            }
        }

        // 3️⃣ Tạo Authentication
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.toString()));
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalEmail, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4️⃣ Tạo JWT
        String token = jwtProvider.generateToken(authentication);

        // 5️⃣ Xóa OTP sau khi dùng (khuyến nghị)
        verificationRepository.delete(verificationCode);

        return new AuthResponse(token, "Login successful", role);
    }



    @Override
    public void sentSignOtp(String email) {
        if(userRepository.existsByEmail(email)) {
            throw new UserException("Email đã tồn tại!");
        }
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

    @Override
    public String createSeller(SellerSignupRequest req) {
        VerificationCode verificationCode = verificationRepository.findByEmail(req.getEmail());
        if(verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())){
            throw new SellerException("Sai mã otp");
        }

        if(sellerRepository.existsByEmail(req.getEmail())){
            throw new SellerException("Email đã tồn tại");
        }

        // Tạo seller
        Seller newSeller = new Seller();
        newSeller.setSellerName(req.getFullName());
        newSeller.setEmail(req.getEmail());
        newSeller.setMobile(req.getMobile());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setPassword(passwordEncoder.encode(req.getOtp()));
        newSeller.setPickUpAddress(req.getAddress()); // nếu cascade = ALL

        // Lưu seller
        Seller seller = sellerRepository.save(newSeller);

        // Xóa OTP sau khi dùng
        verificationRepository.delete(verificationCode);

        // Tạo Authentication và JWT
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(seller.getRole().toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(seller.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }

    @Override
    public String createUser(UserSignupRequest req) throws Exception {
        // Kiểm tra OTP
        VerificationCode verificationCode = verificationRepository.findByEmail(req.getEmail());
        if(verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())){
            throw new UserException("Wrong OTP");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new UserException("Email already exists!");
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


}
