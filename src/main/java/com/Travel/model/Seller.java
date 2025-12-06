package com.Travel.model;

import com.Travel.domain.AccountStatus;
import com.Travel.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)//tu tang id
    private Long id;

    private String sellerName;
    private String mobile;

    @NotNull
    @Column(unique = true)
    private String email;
    private String password;

    //địa chỉ mà khách sẽ đến để bắt đầu tour
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pickup_address_id")
    private Address pickUpAddress = new Address();

    //ten cty
    private String companyName;

    private USER_ROLE role;

    private AccountStatus status = AccountStatus.PENDING_VERIFICATION;

    @Embedded
    private BusinessDetail businessDetails = new BusinessDetail();

    @Embedded
    private BankDetail bankDetails = new BankDetail();

}
