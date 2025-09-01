package com.example.accountservice.repository;

import com.example.accountservice.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);  // ✅ multiple accounts per user
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
