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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
//UserDetailService la ham co san , la cau noi xac thuc giua database va securutiy
public class CustomUserServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            return buildUserDetails(user.getEmail(), user.getPassword(), user.getRole());
        }

        // 2️⃣ Nếu không có user → tìm trong bảng Seller
        Seller seller = sellerRepository.findByEmail(email);
        if (seller != null) {
            return buildUserDetails(seller.getEmail(), seller.getPassword(), seller.getRole());
        }

        // 3️⃣ Nếu không có trong cả hai → ném exception
        throw new UsernameNotFoundException("User or Seller not found with email: " + email);
    }
    private UserDetails buildUserDetails(String email, String password, USER_ROLE role) {
        if (role == null) role = USER_ROLE.ROLE_CUSTOMER;

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.toString()));

        return new org.springframework.security.core.userdetails.User(
                email,
                password,
                authorities
        );
    }

}
