package com.Travel.request;

import lombok.Data;

@Data //tu tao getter setter
public class SignupRequest {
    private String email;
    private String fullName;
    private String mobile;
    private String otp;
}
