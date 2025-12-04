package com.Travel.service;

import com.Travel.model.Seller;

public interface SellerService {
    Seller getSellerProfile(String jwt);
    Seller createSeller(Seller seller);
    Seller getSellerById(Long sellerId);
    Seller updateSeller(Long sellerId,Seller seller);
    void deleteSeller(Long sellerId);
}
