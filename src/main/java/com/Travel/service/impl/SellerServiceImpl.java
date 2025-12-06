package com.Travel.service.impl;

import com.Travel.config.JwtProvider;
import com.Travel.domain.AccountStatus;
import com.Travel.domain.USER_ROLE;
import com.Travel.exceptions.SellerException;
import com.Travel.model.Address;
import com.Travel.model.BankDetail;
import com.Travel.model.Seller;
import com.Travel.repository.AddressRepository;
import com.Travel.repository.SellerRepository;
import com.Travel.request.UpdateSellerRequest;
import com.Travel.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Seller getSellerProfile(String jwt) {
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        Seller seller=getSellerByEmail(email);
        return seller;
    }


    @Override
    public Seller getSellerById(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(()-> new SellerException("Không tìm thấy Seller với id : "+sellerId));
    }

    @Override
    public Seller getSellerByEmail(String email) {
        Seller seller=sellerRepository.findByEmail(email);
        if(seller==null){
            throw new SellerException("Không tìm thấy Email.."+email);
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSeller(AccountStatus status) {
        return sellerRepository.findByStatus(status);
    }



    @Override
    public Seller updateSeller(UpdateSellerRequest req,Long sellerId) {
//        Seller exitsingSeller =this.getSellerById(sellerId);
//        String email   = exitsingSeller.getEmail();
//
//
        return null;
    }

    @Override
    public void deleteSeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId).orElseThrow(()->
                new SellerException("Không tìm thấy seller với ID: " + sellerId));
        sellerRepository.delete(seller);
    }
}
