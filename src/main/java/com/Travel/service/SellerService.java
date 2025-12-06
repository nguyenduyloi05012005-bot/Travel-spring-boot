package com.Travel.service;

import com.Travel.domain.AccountStatus;
import com.Travel.model.Seller;
import com.Travel.request.UpdateSellerRequest;

import java.util.List;

public interface SellerService {
    Seller getSellerProfile(String jwt);
//    Seller createSeller(Seller seller);
    Seller getSellerById(Long sellerId);
    Seller getSellerByEmail(String email);
    List<Seller> getAllSeller(AccountStatus status);
    Seller updateSeller(UpdateSellerRequest req,Long sellerId);
    void deleteSeller(Long sellerId);
}
