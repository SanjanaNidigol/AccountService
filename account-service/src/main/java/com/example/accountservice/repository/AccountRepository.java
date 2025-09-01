package com.example.accountservice.repository;

import com.example.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);
    List<Account> findByCurrencyCode(String currencyCode);

    List<Account> findByOpeningDateAfter(LocalDate date);

    List<Account> findByOpeningDateBefore(LocalDate date);

    List<Account> findByOpeningDateBetween(LocalDate startDate, LocalDate endDate);

    List<Account> findByUserIdAndCurrencyCode(Long userId, String currencyCode);

    List<Account> findByBalanceGreaterThan(BigDecimal amount);

    List<Account> findByBalanceLessThan(BigDecimal amount);
}


//package com.example.accountservice.repository;
//
//import com.example.accountservice.entity.Account;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface AccountRepository extends JpaRepository<Account, Long> {
//    Optional<Account> findByAccountNumber(String accountNumber);
//}
