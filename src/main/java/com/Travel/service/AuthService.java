package com.Travel.service;

import com.Travel.domain.USER_ROLE;
import com.Travel.request.LoginRequest;
import com.Travel.request.SellerSignupRequest;
import com.Travel.request.UserSignupRequest;
import com.Travel.response.AuthResponse;

public interface AuthService {
    void sentLoginOtp(String email) throws Exception;
    String createUser(UserSignupRequest req) throws Exception;
    AuthResponse signing(LoginRequest req) throws Exception;
    void sentSignOtp(String email);
    String createSeller(SellerSignupRequest req);

}
