package com.uditha.loan_service.controller;

import com.uditha.loan_service.dto.*;
import com.uditha.loan_service.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("v1/api/loan")
public class LoanController {
    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/loans")
    public ResponseEntity<LoanRequestResponseDTO> createLoan(@RequestBody LoanRequest request) {
        return ResponseEntity.ok(loanService.createLoan(request));
    }

    @PostMapping("/payments")
    public ResponseEntity<PaymentResponse> registerPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(loanService.registerPayment(request));
    }

    @GetMapping("/report/{customerId}")
    public ResponseEntity<List<LoanReportResponse>> getReport(@Valid @PathVariable Integer customerId) {
        return ResponseEntity.ok(loanService.generateReport(customerId));
    }
}
