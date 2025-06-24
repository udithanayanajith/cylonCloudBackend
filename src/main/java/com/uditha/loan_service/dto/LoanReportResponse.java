package com.uditha.loan_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanReportResponse {
    private String customerName;
    private String mobileNo;
    private int loanNo;
    private double loanAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalPaidAmount;
    private double lastPaidAmount;
    private double totalArrears;
    private double totalOutstanding;
}
