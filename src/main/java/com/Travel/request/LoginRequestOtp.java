package com.Travel.request;

import com.Travel.domain.USER_ROLE;
import lombok.Data;

@Data
public class LoginRequestOtp {
    private String email;
    private String otp;
}
