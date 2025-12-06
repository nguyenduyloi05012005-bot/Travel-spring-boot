package com.Travel.service.impl;

import com.Travel.domain.USER_ROLE;
import com.Travel.model.Seller;
import com.Travel.model.User;
import com.Travel.repository.SellerRepository;
import com.Travel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1) Tìm user
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return toUserDetails(user.getEmail(), user.getPassword(), user.getRole());
        }

        // 2) Tìm seller
        Seller seller = sellerRepository.findByEmail(email);
        if (seller != null) {
            return toUserDetails(seller.getEmail(), seller.getPassword(), seller.getRole());
        }

        // 3) Không tìm thấy
        throw new UsernameNotFoundException("User or Seller not found with email: " + email);
    }

    private UserDetails toUserDetails(String email, String password, USER_ROLE role) {
        if (role == null) role = USER_ROLE.ROLE_CUSTOMER;  // fallback tránh null

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(role.toString()));

        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                authorities
        );
    }
}
