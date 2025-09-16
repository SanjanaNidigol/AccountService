package com.example.accountservice.controller;

import com.example.accountservice.entity.Account;
import com.example.accountservice.entity.Account.AccountType;
import com.example.accountservice.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        AccountType accountType = AccountType.valueOf(request.get("accountType").toString().toUpperCase());
        String currencyCode = request.get("currencyCode").toString();

        BigDecimal balance = request.containsKey("balance")
                ? new BigDecimal(request.get("balance").toString())
                : BigDecimal.ZERO;

        return ResponseEntity.ok(
                service.createAccount(userId, accountType, balance, currencyCode)
        );
    }

    // GET - Get all accounts for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getAccountsByUserId(userId));
    }

    // GET - Get account by ID
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getAccountById(id));
    }

    // GET - Get all accounts (admin use case)
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(service.getAllAccounts());
    }

    // PUT - Debit account
    @PutMapping("/{id}/debit")
    public ResponseEntity<?> debit(@PathVariable Long id, @RequestParam BigDecimal amount) {
        try {
            return ResponseEntity.ok(service.debitAccount(id, amount));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // PUT - Credit account
    @PutMapping("/{id}/credit")
    public ResponseEntity<?> credit(@PathVariable Long id, @RequestParam BigDecimal amount) {
        try {
            return ResponseEntity.ok(service.creditAccount(id, amount));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // GET - Accounts by currency
    @GetMapping("/currency/{code}")
    public ResponseEntity<List<Account>> getAccountsByCurrency(@PathVariable String code) {
        return ResponseEntity.ok(service.getAccountsByCurrency(code));
    }

    // GET - Accounts opened after a date
    @GetMapping("/opened-after/{date}")
    public ResponseEntity<List<Account>> getAccountsOpenedAfter(@PathVariable String date) {
        LocalDate d = LocalDate.parse(date);
        return ResponseEntity.ok(service.getAccountsOpenedAfter(d));
    }

    // GET - Accounts opened between two dates
    @GetMapping("/opened-between")
    public ResponseEntity<List<Account>> getAccountsOpenedBetween(
            @RequestParam String start,
            @RequestParam String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return ResponseEntity.ok(service.getAccountsOpenedBetween(startDate, endDate));
    }

    // GET - Accounts by user and currency
    @GetMapping("/user/{userId}/currency/{code}")
    public ResponseEntity<List<Account>> getAccountsByUserAndCurrency(
            @PathVariable Long userId,
            @PathVariable String code) {
        return ResponseEntity.ok(service.getAccountsByUserAndCurrency(userId, code));
    }

    // GET - Accounts with balance greater than
    @GetMapping("/balance/greater-than")
    public ResponseEntity<List<Account>> getAccountsWithBalanceGreaterThan(@RequestParam BigDecimal amount) {
        return ResponseEntity.ok(service.getAccountsWithBalanceGreaterThan(amount));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<Map<String, Object>> getAccountBalance(@PathVariable Long id) {
        Account acc = service.getAccountById(id);
        if (acc != null) {
            return ResponseEntity.ok(Map.of("balance", acc.getBalance()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{accountId}/number")
    public ResponseEntity<String> getAccountNumberByAccountId(@PathVariable Long accountId) {
        Account account = service.getAccountById(accountId);
        if (account != null) {
            return ResponseEntity.ok(account.getAccountNumber());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/id/byNumber/{accountNumber}")
    public ResponseEntity<Long> getAccountIdByNumber(@PathVariable String accountNumber) {
        try {
            Account account = service.getAccountByNumber(accountNumber);
            return ResponseEntity.ok(account.getAccountId());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{accountId}/userId")
    public ResponseEntity<Long> getUserIdByAccountId(@PathVariable Long accountId) {
        Account acc = service.getAccountById(accountId);
        if (acc != null) {
            return ResponseEntity.ok(acc.getUserId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/byNumber/{accountNumber}")
    public ResponseEntity<Account> getAccountByNumber(@PathVariable String accountNumber) {
        Account account = service.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(account);
    }


}