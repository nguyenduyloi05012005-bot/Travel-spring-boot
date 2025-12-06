package com.Travel.request;

import com.Travel.model.Address;
import com.Travel.model.BankDetail;
import com.Travel.model.BusinessDetail;
import lombok.Data;

@Data
public class UpdateSellerRequest {
//    private String email;
    private String sellerName;
    private String mobile;
    private String otp;
    private Address address;
    private BusinessDetail businessDetail;
    private BankDetail bankDetail;
}
