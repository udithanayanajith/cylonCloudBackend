package com.uditha.loan_service.service;

import com.uditha.loan_service.dto.*;
import com.uditha.loan_service.repository.LoanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {
    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public LoanRequestResponseDTO createLoan(LoanRequest request) {
        return loanRepository.saveNewLoan(request);
    }

    public PaymentResponse registerPayment(PaymentRequest request) {
        return loanRepository.updatePayment(request);
    }

    public List<LoanReportResponse> generateReport(Integer customerId) {
      return loanRepository.fetchReport(customerId);
    }

    public List<CustomerResponse> getAllCustomers() {
        return loanRepository.getAllCustomers();
    }
}
