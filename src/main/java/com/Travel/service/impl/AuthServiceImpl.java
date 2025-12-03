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
        String SIGNING_PREFIX="signin_";//gia su co 1 thg dang signin_loi1590@gmail.com
        if(email.startsWith(SIGNING_PREFIX)){
            email=email.substring(SIGNING_PREFIX.length());
            if(role.equals(USER_ROLE.ROLE_SELLER)){
                Seller seller= sellerRepository.findByEmail(email);
                if (seller==null){
                    throw new Exception("Seller email Not Found!");
                }
            }
            else{
                User user= userRepository.findByEmail(email);
                if(user==null){
                    throw new Exception("User email not found!");
                }
            }
        }
        //neu dang dang ki
        VerificationCode isExists=verificationRepository.findByEmail(email);// set cai isExit = cai email;
        if(isExists != null){
            verificationRepository.delete(isExists);
        }
        String otp= OtpUtil.generateOtp();//tao 1 otp moi
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationRepository.save(verificationCode);
        String subject = "Welcome To Travel Ecommerce";
        String text = "Here you otp: " + otp;
        emailService.sendVerificationOtpMail(email,otp,subject,text);
    }

    @Override
    public String createUser(SignupRequest req) throws Exception {
        VerificationCode verificationCode=verificationRepository.findByEmail(req.getEmail());
        if(verificationCode==null|| !verificationCode.getOtp().equals(req.getOtp())){
            throw new Exception("Wrong otp...");
        }
        User user= userRepository.findByEmail(req.getEmail());
        if(user==null){
            User createUser = new User();
            createUser.setEmail(req.getEmail());
            createUser.setFullName(req.getFullName());
            createUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            createUser.setMobile(req.getMobile());
            createUser.setPassword(passwordEncoder.encode(req.getOtp()));
            user=userRepository.save(createUser);

            //con` thieu cart
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(USER_ROLE.ROLE_CUSTOMER.toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(),null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }

    @Override
    public AuthResponse signing(LoginRequest req) {
        String userName=req.getEmail();
        String otp =req.getOtp();
        Authentication authentication = authenticate
        return null;
    }

    private final Authentication authenticate(String userName,String otp){
        UserDetails userDetails=cu
    }
}
