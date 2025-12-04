package com.Travel.service;

import com.Travel.domain.USER_ROLE;
import com.Travel.request.LoginRequest;
import com.Travel.request.SignupRequest;
import com.Travel.response.AuthResponse;

public interface AuthService {
    void sentLoginOtp(String email,USER_ROLE role) throws Exception;
    String createUser(SignupRequest req) throws Exception;
    AuthResponse signing(LoginRequest req) throws Exception;
    void sentSignOtp(String email);

}
