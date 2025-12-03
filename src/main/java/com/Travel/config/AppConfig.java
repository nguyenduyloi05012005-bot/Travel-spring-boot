package com.Travel.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
@Configuration
@EnableWebSecurity
public class AppConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())

                // ⚙️ Không dùng session, mỗi request cần tự mang token (stateless)
                .sessionManagement(management ->
                        management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 🔐 Phân quyền truy cập URL
                .authorizeHttpRequests(authorize -> authorize
                        // Cho phép truy cập công khai (không cần đăng nhập)
                        .requestMatchers("/api/products/*/reviews").permitAll()

                        // Các URL bắt đầu bằng /api/** phải đăng nhập (có token)
                        .requestMatchers("/api/**").authenticated()

                        // Các URL còn lại ai cũng truy cập được
                        .anyRequest().permitAll())

                // 🧾 Thêm JWT Filter để kiểm tra token trước khi vào Spring Security
                .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)

                // 🌐 Bật CORS để frontend gọi được backend
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    // 🌍 Cấu hình CORS chi tiết
    private CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();

                // ✅ Cho phép gọi API từ mọi domain (như React ở localhost:3000)
                cfg.setAllowedOrigins(Collections.singletonList("*"));

                // ✅ Cho phép mọi phương thức HTTP: GET, POST, PUT, DELETE,...
                cfg.setAllowedMethods(Collections.singletonList("*"));

                // ✅ Cho phép mọi header (Authorization, Content-Type, ...)
                cfg.setAllowedHeaders(Collections.singletonList("*"));

                // ✅ Cho phép gửi cookie/token kèm request
                cfg.setAllowCredentials(true);

                // ✅ Cho phép client đọc được header Authorization trong response
                cfg.setExposedHeaders(Collections.singletonList("Authorization"));

                // ✅ Trình duyệt cache CORS config này trong 1 giờ
                cfg.setMaxAge(3600L);

                return cfg;
            }
        };
    }

    @Bean
    PasswordEncoder passwordEncoder(){ //mã hóa mật khẩu , ví du nhập 123,spring boot sẽ luu vào db $2a$10$4dVJ..
        //hacker sẽ k biết mã hóa là gì
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate(){//hàm tao ra cái máy gọi api khác từ sever của mình ví dụ thời tiết...
        return new RestTemplate();
    }
}
