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

    @OneToOne
    private Address pickUpAddress = new Address();

    //ten cty
    private String companyName;

    private USER_ROLE role;

    private boolean isEmailVerified = false; // mac dinh do xac thuc la false

    private AccountStatus status = AccountStatus.PENDING_VERIFICATION;

    //Ngan hang mot code sau //nhúng class nhỏ vào class lớn , các flieds của class nhỏ sẽ thành cột của class lớn cho đỡ rối
//    @Embedded
//    private BusinessDetails businessDetails = new BusinessDetails();

//    @Embedded
//    private BankDetails bankDetails = new BankDetails();

}
