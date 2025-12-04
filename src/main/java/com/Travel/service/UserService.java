package com.Travel.service;

import com.Travel.model.User;

public interface UserService {
    User findUserByJwt(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
}
