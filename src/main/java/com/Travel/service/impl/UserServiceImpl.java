package com.Travel.service.impl;

import com.Travel.config.JwtProvider;
import com.Travel.model.User;
import com.Travel.repository.UserRepository;
import com.Travel.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    @Override
    public User findUserByJwt(String jwt) throws Exception {
        String email=jwtProvider.getEmailFromJwtToken(jwt);
        return this.findUserByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user=userRepository.findByEmail(email);
        if(user==null){
            throw new Exception("User not found with the email" + email);
        }
        return user;
    }
}
