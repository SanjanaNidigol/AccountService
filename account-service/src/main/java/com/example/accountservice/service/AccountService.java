package com.example.accountservice.service;

import com.example.accountservice.entity.Account;
import com.example.accountservice.repository.AccountRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    private final AccountRepository repo;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public AccountService(AccountRepository repo, KafkaTemplate<String, String> kafkaTemplate) {
        this.repo = repo;
        this.kafkaTemplate = kafkaTemplate;
    }

    // Generate 12-digit account number that doesn't start with 0
    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // Ensure the first digit is not 0
        sb.append(random.nextInt(9) + 1);

        // Append the remaining 11 digits
        for (int i = 0; i < 11; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }

//    // Generate 12-digit account number
//    private String generateAccountNumber() {
//        Random random = new Random();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < 12; i++) {
//            sb.append(random.nextInt(10));
//        }
//        return sb.toString();
//    }

    // Create new account and publish Kafka event
    public Account createAccount(Long userId, String type, BigDecimal balance, String currencyCode) {
        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(balance != null ? balance : BigDecimal.ZERO)
                .userId(userId)
                .accountType(type)
                .currencyCode(currencyCode)      // add this
                .openingDate(LocalDate.now())
                .build();

        Account savedAccount = repo.save(account);

        // Publish event to Kafka
        String message = "ACCOUNT_CREATED:" + savedAccount.getAccountId();
        kafkaTemplate.send("account-events", message);

        return savedAccount;
    }

    // Get all accounts by user
    public List<Account> getAccountsByUserId(Long userId) {
        return repo.findByUserId(userId);
    }

    // Get all accounts (Admin use case)
    public List<Account> getAllAccounts() {
        return repo.findAll();
    }

    // Get account by ID
    public Account getAccountById(Long id) {
        return repo.findById(id).orElse(null);
    }

    // Debit account and publish Kafka event
    public Account debitAccount(Long id, BigDecimal amount) {
        Account acc = getAccountById(id);
        if (acc.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        acc.setBalance(acc.getBalance().subtract(amount));
        Account updated = repo.save(acc);

        // Publish debit event
        String message = "ACCOUNT_DEBITED:" + id + ",amount=" + amount;
        kafkaTemplate.send("account-events", message);

        return updated;
    }

    // Credit account and publish Kafka event
    public Account creditAccount(Long id, BigDecimal amount) {
        Account acc = getAccountById(id);
        acc.setBalance(acc.getBalance().add(amount));
        Account updated = repo.save(acc);

        // Publish credit event
        String message = "ACCOUNT_CREDITED:" + id + ",amount=" + amount;
        kafkaTemplate.send("account-events", message);

        return updated;
    }

    // Get accounts by currency
    public List<Account> getAccountsByCurrency(String currencyCode) {
        return repo.findByCurrencyCode(currencyCode);
    }

    // Get accounts opened after a date
    public List<Account> getAccountsOpenedAfter(LocalDate date) {
        return repo.findByOpeningDateAfter(date);
    }

    // Get accounts opened before a date
    public List<Account> getAccountsOpenedBefore(LocalDate date) {
        return repo.findByOpeningDateBefore(date);
    }

    // Get accounts opened between two dates
    public List<Account> getAccountsOpenedBetween(LocalDate start, LocalDate end) {
        return repo.findByOpeningDateBetween(start, end);
    }

    // Get accounts by user and currency
    public List<Account> getAccountsByUserAndCurrency(Long userId, String currencyCode) {
        return repo.findByUserIdAndCurrencyCode(userId, currencyCode);
    }

    // Get accounts by balance greater than
    public List<Account> getAccountsWithBalanceGreaterThan(BigDecimal amount) {
        return repo.findByBalanceGreaterThan(amount);
    }

    // Get accounts by balance less than
    public List<Account> getAccountsWithBalanceLessThan(BigDecimal amount) {
        return repo.findByBalanceLessThan(amount);
    }

}


//package com.example.accountservice.service;
//
//import com.example.accountservice.entity.Account;
//import com.example.accountservice.repository.AccountRepository;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Random;
//
//@Service
//public class AccountService {
//
//    private final AccountRepository repo;
//
//    public AccountService(AccountRepository repo) {
//        this.repo = repo;
//    }
//
////    public Account createAccount(Account account) {
////        account.setAccountNumber(generateAccountNumber());
////
////        // Keep the provided balance, only set 0 if null
////        if (account.getBalance() == null) {
////            account.setBalance(BigDecimal.valueOf(0.0));
////        }
////
////        return repo.save(account);
////    }
//
//
//    // Generate 12-digit account number
//    private String generateAccountNumber() {
//        Random random = new Random();
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < 12; i++) {
//            sb.append(random.nextInt(10));
//        }
//        return sb.toString();
//    }
//
//
//    public Account createAccount(Long userId, String type, BigDecimal balance) {
//        Account account = Account.builder()
//                .accountNumber(generateAccountNumber())
//                .balance(balance != null ? balance : BigDecimal.ZERO)
//                .userId(userId)
//                .accountType(type)
//                .build();
//
//        return repo.save(account);
//    }
//
//
//    // Create new account
////    public Account createAccount(Long userId, String type) {
////        Account account = Account.builder()
////                .accountNumber(generateAccountNumber())
////                .balance(BigDecimal.ZERO)
////                .userId(userId)
////                .accountType(type)
////                .build();
////
////        return repo.save(account);  // ✅ always inserts new row
////    }
//
//    // Get all accounts by user
//    public List<Account> getAccountsByUserId(Long userId) {
//        return repo.findByUserId(userId);
//    }
//
//    // Get all accounts (Admin use case)
//    public List<Account> getAllAccounts() {
//        return repo.findAll();
//    }
//
//    // Get account by ID
//    public Account getAccountById(Long id) {
//        return repo.findById(id).orElse(null);
//    }
//
//
//    public Account debitAccount(Long id, BigDecimal amount) {
//        Account acc = getAccountById(id);
//        if (acc.getBalance().compareTo(amount) < 0) {
//            throw new RuntimeException("Insufficient balance");
//        }
//        acc.setBalance(acc.getBalance().subtract(amount));
//        return repo.save(acc);
//    }
//
//    public Account creditAccount(Long id, BigDecimal amount) {
//        Account acc = getAccountById(id);
//        acc.setBalance(acc.getBalance().add(amount));
//        return repo.save(acc);
//    }
//
//
//}
//
//
////package com.example.accountservice.service;
////
////import com.example.accountservice.entity.Account;
////import com.example.accountservice.repository.AccountRepository;
////import lombok.RequiredArgsConstructor;
////import org.springframework.kafka.core.KafkaTemplate;
////import org.springframework.stereotype.Service;
////import org.springframework.transaction.annotation.Transactional;
////
////import java.math.BigDecimal;
////
////@Service
////@RequiredArgsConstructor
////public class AccountService {
////    private final AccountRepository repo;
////    private final KafkaTemplate<String, String> kafka;
////
////    @Transactional
////    public Account create(Long userId, String accountNumber) {
////        Account a = Account.builder().userId(userId).accountNumber(accountNumber).balance(BigDecimal.ZERO).build();
////        a = repo.save(a);
////        kafka.send("account-events", "ACCOUNT_CREATED:" + a.getId());
////        return a;
////    }
////
////    @Transactional
////    public Account deposit(String accountNumber, BigDecimal amount) {
////        Account a = repo.findByAccountNumber(accountNumber).orElseThrow();
////        a.setBalance(a.getBalance().add(amount));
////        kafka.send("account-events", "DEPOSIT:" + a.getId() + ":" + amount);
////        return repo.save(a);
////    }
////}
