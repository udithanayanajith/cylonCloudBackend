package com.uditha.loan_service.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private int customerId;
    private int scheduleId;
    private double paidAmount;
}
