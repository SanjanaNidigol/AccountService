package com.example.accountservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")  // changed column name
    private Long accountId;


    @Column(name = "account_number", nullable = false, unique = true, length = 12)
    private String accountNumber; // 12 digits number

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Column(name = "user_id", nullable = false)
    private Long userId;   // ✅ NOT unique → allows multiple accounts per user

//    @Column(name = "account_type", nullable = false)
//    private String accountType; // SAVINGS / CURRENT SALARY FIXED_DEPOSIT

    @Enumerated(EnumType.STRING) // This annotation tells JPA to store the enum name as a String
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Column(name = "opening_date", nullable = false)
    private LocalDate openingDate;

    public enum AccountType {
        SAVINGS,
        CURRENT,
        SALARY,
        FIXED_DEPOSIT,
        NRE_SAVINGS,
        NRO_SAVINGS
    }
}


//package com.example.accountservice.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.math.BigDecimal;
//
//@Entity
//@Table(name = "accounts")
//@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
//public class Account {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private Long userId;
//
//    @Column(nullable = false, unique = true, length = 12)
//    private String accountNumber;
//
//    @Column(nullable = false, precision = 19, scale = 2)
//    private BigDecimal balance = BigDecimal.ZERO;
//}
