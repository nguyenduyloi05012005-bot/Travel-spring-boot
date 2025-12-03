package com.Travel.model;

import com.Travel.domain.USER_ROLE;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//serve se an password roi moi gui cho client
    private String password;
    private String email;
    private String fullName;
    private String mobile;
    private USER_ROLE role =USER_ROLE.ROLE_CUSTOMER;

    @ManyToMany
    private Set<Address> addresses= new HashSet<>();

//    //con` thieu coupon chua tao nua
//    @ManyToMany
//    @JsonIgnore // k hien o client
//    private Set<Coupon> usedCoupons = new HashSet<>();
}
