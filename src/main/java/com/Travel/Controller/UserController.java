package com.Travel.Controller;

import com.Travel.model.User;
import com.Travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.text.html.parser.Entity;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("user/profile")
    public ResponseEntity<User> UserHandler(@RequestHeader("Authorization") String authHeader) throws Exception {
        String jwt = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        User user = userService.findUserByJwt(jwt);
        return ResponseEntity.ok(user);
    }
}
