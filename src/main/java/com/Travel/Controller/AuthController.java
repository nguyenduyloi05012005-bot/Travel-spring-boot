package com.Travel.Controller;

import com.Travel.domain.USER_ROLE;
import com.Travel.repository.UserRepository;
import com.Travel.request.LoginRequest;
import com.Travel.request.LoginRequestOtp;
import com.Travel.request.SellerSignupRequest;
import com.Travel.request.UserSignupRequest;
import com.Travel.response.ApiResponse;
import com.Travel.response.AuthResponse;
import com.Travel.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {
    private final UserRepository userRepository;
    private final AuthService authService;

    //ham xu li dang ki user
    @PostMapping("/user/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody UserSignupRequest req) throws Exception {
        String jwt=authService.createUser(req);
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("Đăng kí thành công ");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);
        return ResponseEntity.ok(res);
    }

    //xu li dang ki seller
    @PostMapping("/seller/signup")
    public ResponseEntity<AuthResponse> createSellerHandler(@RequestBody SellerSignupRequest req){
        String jwt = authService.createSeller(req);
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("Đangư kí thành cong");
        res.setRole(USER_ROLE.ROLE_SELLER);
        return ResponseEntity.ok(res);
    }
    //gui otp cho dang nhap
    @PostMapping("/send/login-login-otp")
    public ResponseEntity<ApiResponse> sentOtpLoginHandler(@RequestBody LoginRequestOtp req) throws Exception {
        authService.sentLoginOtp(req.getEmail()); // chỉ truyền email
        ApiResponse res = new ApiResponse();
        res.setMessage("Otp đã gửi thành công");
        return ResponseEntity.ok(res);
    }

    //gui otp cho dang ki
    @PostMapping("/send/signup-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpSignupHandler(@RequestBody LoginRequest req){
        authService.sentSignOtp(req.getEmail());
        ApiResponse res = new ApiResponse();
        res.setMessage("Otp đã gửi thành công ");
        return ResponseEntity.ok(res);
    }
    //dang nhap dung` chung
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req) throws Exception {
        AuthResponse authResponse=authService.signing(req);
        return ResponseEntity.ok(authResponse);
    }
}
