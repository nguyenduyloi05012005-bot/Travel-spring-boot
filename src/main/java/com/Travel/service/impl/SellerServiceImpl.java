package com.Travel.service.impl;

import com.Travel.model.Seller;
import com.Travel.repository.SellerRepository;
import com.Travel.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;

    @Override
    public Seller getSellerProfile(String jwt) {

        return null;
    }

    @Override
    public Seller createSeller(Seller seller) {
        return null;
    }

    @Override
    public Seller getSellerById(Long sellerId) {
        return null;
    }

    @Override
    public Seller updateSeller(Long sellerId, Seller seller) {
        return null;
    }

    @Override
    public void deleteSeller(Long sellerId) {

    }
}
