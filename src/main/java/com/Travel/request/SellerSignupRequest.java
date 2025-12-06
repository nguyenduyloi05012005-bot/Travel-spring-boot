package com.Travel.request;

import com.Travel.model.Address;
import lombok.Data;

@Data
public class SellerSignupRequest {
    private String email;
    private String fullName;
    private String mobile;
    private String otp;
    private Address address;
}
