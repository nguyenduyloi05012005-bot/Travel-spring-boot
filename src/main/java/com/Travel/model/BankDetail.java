package com.Travel.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BankDetail {
    private String accountNumber;
    private String accountHolderName;
    private String bankName;
}
