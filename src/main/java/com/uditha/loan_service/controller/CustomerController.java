package com.uditha.loan_service.controller;

import com.uditha.loan_service.dto.CustomerResponse;
import com.uditha.loan_service.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("v1/api/cus")
public class CustomerController {
    private final LoanService loanService;

    public CustomerController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(){
        return ResponseEntity.ok(loanService.getAllCustomers());

    }
}
