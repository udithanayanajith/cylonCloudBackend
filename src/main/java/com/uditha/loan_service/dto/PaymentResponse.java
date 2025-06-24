package com.uditha.loan_service.dto;

import lombok.Data;

@Data
public class PaymentResponse {
    private double dueAmount;
    private int loanId;
    private double lastPayment;
}
