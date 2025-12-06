package com.Travel.request;

import lombok.Data;

    @Data //tu tao getter setter
    public class UserSignupRequest {
        private String email;
        private String fullName;
        private String mobile;
        private String otp;
    }
