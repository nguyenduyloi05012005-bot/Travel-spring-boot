package com.Travel.Controller;

import com.Travel.domain.USER_ROLE;
import com.Travel.repository.UserRepository;
import com.Travel.request.LoginRequest;
import com.Travel.request.LoginRequestOtp;
import com.Travel.request.SignupRequest;
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

    //ham xu li dang ki
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception {
        String jwt=authService.createUser(req);
        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("Đăng kí thành ");
        res.setRole(USER_ROLE.ROLE_CUSTOMER);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/send/login-login-otp")
    public ResponseEntity<ApiResponse> sentOtpLoginHandler(@RequestBody LoginRequestOtp req) throws Exception {
        authService.sentLoginOtp(req.getEmail(),req.getRole());
        ApiResponse res= new ApiResponse();
        res.setMessage("Otp đã gửi thành công");
        return  ResponseEntity.ok(res);
    }

    @PostMapping("/send/signup-signup-otp")
    public ResponseEntity<ApiResponse> sentOtpSignupHandler(@RequestBody LoginRequest req){
        authService.sentSignOtp(req.getEmail());
        ApiResponse res = new ApiResponse();
        res.setMessage("Otp đã gửi thành ");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req) throws Exception {
        AuthResponse authResponse=authService.signing(req);
        return ResponseEntity.ok(authResponse);
    }
}
