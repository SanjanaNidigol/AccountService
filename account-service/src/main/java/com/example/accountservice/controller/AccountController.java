package com.example.accountservice.controller;

import com.example.accountservice.entity.Account;
import com.example.accountservice.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    // POST - Create account
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        String accountType = request.get("accountType").toString();

        // Extract balance if present, else default to 0
        BigDecimal balance = request.containsKey("balance")
                ? new BigDecimal(request.get("balance").toString())
                : BigDecimal.ZERO;

        return ResponseEntity.ok(service.createAccount(userId, accountType, balance));
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
}


//package com.example.accountservice.controller;
//
//import com.example.accountservice.entity.Account;
//import com.example.accountservice.service.AccountService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/accounts")
//public class AccountController {
//
//    private final AccountService service;
//
//    public AccountController(AccountService service) {
//        this.service = service;
//    }
//
//    // POST - Create account
//    @PostMapping
//    public ResponseEntity<Account> createAccount(@RequestBody Map<String, Object> request) {
//        Long userId = Long.valueOf(request.get("userId").toString());
//        String accountType = request.get("accountType").toString();
//
//        // Extract balance from the request body, handling the case where it might be null
//        BigDecimal balance = null;
//        if (request.containsKey("balance")) {
//            balance = new BigDecimal(request.get("balance").toString());
//        }
//
//        // Pass all three arguments to the service method
//        return ResponseEntity.ok(service.createAccount(userId, accountType, balance));
//    }
//
//    // GET - Get all accounts for a user
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Account>> getAccountsByUser(@PathVariable Long userId) {
//        return ResponseEntity.ok(service.getAccountsByUserId(userId));
//    }
//
//    // GET - Get account by ID
//    @GetMapping("/{id}")
//    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
//        return ResponseEntity.ok(service.getAccountById(id));
//    }
//
//    // GET - Get all accounts (admin use case)
//    @GetMapping
//    public ResponseEntity<List<Account>> getAllAccounts() {
//        return ResponseEntity.ok(service.getAllAccounts());
//    }
//
//
//
//}
//
////package com.example.accountservice.controller;
////
////import com.example.accountservice.entity.Account;
////import com.example.accountservice.service.AccountService;
////import jakarta.validation.constraints.NotBlank;
////import jakarta.validation.constraints.NotNull;
////import lombok.Data;
////import lombok.RequiredArgsConstructor;
////import org.springframework.http.ResponseEntity;
////import org.springframework.web.bind.annotation.*;
////
////import java.math.BigDecimal;
////
////@RestController
////@RequestMapping("/api/accounts")
////@RequiredArgsConstructor
////public class AccountController {
////    private final AccountService service;
////
////    @PostMapping
////    public ResponseEntity<Account> create(@RequestBody CreateRequest req) {
////        return ResponseEntity.ok(service.create(req.getUserId(), req.getAccountNumber()));
////    }
////
////    @PostMapping("/deposit")
////    public ResponseEntity<Account> deposit(@RequestBody DepositRequest req) {
////        return ResponseEntity.ok(service.deposit(req.getAccountNumber(), req.getAmount()));
////    }
////
////    @Data
////    public static class CreateRequest { @NotNull Long userId; @NotBlank String accountNumber; }
////    @Data
////    public static class DepositRequest { @NotBlank String accountNumber; @NotNull BigDecimal amount; }
////}
