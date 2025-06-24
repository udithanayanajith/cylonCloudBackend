package com.uditha.loan_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanRequestResponseDTO {
    private int customerId;
    private int loanNo;
    private int loanId;
    private double loanAmount;
    private double interestRate;
    private double rentalAmount;
    private int duration;
    private String scheduleType;
    private LocalDate startDate;
    private LocalDate endDate;
}
