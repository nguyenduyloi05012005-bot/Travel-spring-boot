package com.Travel.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtProvider {
    //token=header.payload.signture
    //class nay tao token va lay token tu` email
    //HMAC hash-based messaged Authentication code tao chu ki bang hash+key
    SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRETKEY.getBytes());

    public String generateToken(Authentication auth){
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles= populateAuthorities(authorities);

        return Jwts.builder()
                .setIssuedAt(new Date())//thoi gian token dc tao ra
                .setExpiration(new Date(new Date().getTime() + 86400000))// thoi gian token het han (1ngay)
                .claim("email",auth.getName())//them du lieu vao payload
                .claim("authorities",roles)//them thong tin quyen han cua user vao payload
                .signWith(key)
                .compact();
    }

    public String getEmailFromJwtToken(String jwt){
        jwt = jwt.substring(7);
        //Bearer Token
        Claims claim = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
        return String.valueOf(claim.get("email"));
    }

    //phân nay xem ng dùng có quyền hạn gì và đc làm gì
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities){
        Set<String> auths=new HashSet<>();//tao 1 cai hashset de loai bo trung nhau
        for(GrantedAuthority authority:authorities){//duyet qua cac Granted de tra ve quyen han
            auths.add(authority.getAuthority());
        }
        return String.join(",",auths);//gom tat ca quyen han thanh 1 chuoi cach nhau 1 dau phay
    }
}
