package com.uditha.loan_service.repository;

import com.uditha.loan_service.dto.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class LoanRepository {

    private final JdbcTemplate jdbcTemplate;

    public LoanRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public LoanRequestResponseDTO saveNewLoan(LoanRequest request) {
        if (request.getDuration() <= 0) {
            throw new RuntimeException("Duration must be positive");
        }

        double rental = calculateRental(request.getLoanAmount(), request.getInterestRate(), request.getDuration());
        BigDecimal roundedRental = BigDecimal.valueOf(rental).setScale(2, RoundingMode.HALF_UP);
        LocalDate endDate = calculateEndDate(request.getStartDate(), request.getDuration(), request.getScheduleType());
        String insertLoan = "INSERT INTO loan (loan_no, loan_amount, interest_rate, duration, rental_amount, start_date, end_date, customer_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(insertLoan,
                request.getLoanNo(),
                request.getLoanAmount(),
                request.getInterestRate(),
                request.getDuration(),
                roundedRental.doubleValue(),
                Date.valueOf(request.getStartDate()),
                Date.valueOf(endDate),
                request.getCustomerId());

        Integer loanId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);

        String insertSchedule = "INSERT INTO loan_schedule (date, due_amount, paid_amount, loan_id) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(insertSchedule,Date.valueOf(request.getStartDate()),
                roundedRental.doubleValue(),
                    0.0,
                    loanId);


        LoanRequestResponseDTO response = new LoanRequestResponseDTO();
        response.setCustomerId(request.getCustomerId());
        response.setLoanNo(request.getLoanNo());
        response.setLoanId(loanId);
        response.setLoanAmount(request.getLoanAmount());
        response.setInterestRate(request.getInterestRate());
        response.setRentalAmount(roundedRental.doubleValue());
        response.setDuration(request.getDuration());
        response.setScheduleType(request.getScheduleType());
        response.setStartDate(request.getStartDate());
        response.setEndDate(endDate);

        return response;
    }

    private LocalDate calculateEndDate(LocalDate startDate, int duration, String scheduleType) {
        if ("monthly".equals(scheduleType)) {
            return startDate.plusMonths(duration);
        } else {
            return startDate.plusWeeks(duration);
        }
    }

    private double calculateRental(double amount, double rate, int duration) {
        if (duration <= 0) {
            throw new RuntimeException("Duration must be positive");
        }
        double totalWithInterest = amount + (amount * rate / 100);
        return totalWithInterest / duration;
    }

    @Transactional
    public PaymentResponse updatePayment(PaymentRequest request) {

        String updateSchedule = "UPDATE loan_schedule SET paid_amount = paid_amount + ?, due_amount = due_amount - ? WHERE schedule_id = ?";
        jdbcTemplate.update(updateSchedule, request.getPaidAmount(), request.getPaidAmount(), request.getScheduleId());

        String getSchedule = "SELECT date, due_amount, paid_amount, loan_id FROM loan_schedule WHERE schedule_id = ?";
        Map<String, Object> scheduleResult = jdbcTemplate.queryForMap(getSchedule, request.getScheduleId());

        String insertPaymentSummary = "INSERT INTO payment_summary (cusId, lastPayment,payment_date) VALUES (?, ?,?)";

        jdbcTemplate.update(insertPaymentSummary, request.getCustomerId(), request.getPaidAmount(), java.sql.Date.valueOf(LocalDate.now()));

        String getLastPayment = "SELECT lastPayment FROM payment_summary WHERE cusId = ? ORDER BY payment_date DESC LIMIT 1";
        Double lastPayment = jdbcTemplate.queryForObject(getLastPayment, Double.class, request.getCustomerId());

        PaymentResponse response = new PaymentResponse();
        response.setDueAmount((Double) scheduleResult.get("due_amount"));
        response.setLoanId((Integer) scheduleResult.get("loan_id"));
        response.setLastPayment(lastPayment);

        return response;
    }
    @Transactional
    public List<LoanReportResponse> fetchReport(Integer customerId) {
        String query = "SELECT " +
                "c.full_name AS customerName, " +
                "c.mobile_no AS mobileNo, " +
                "l.loan_no AS loanNo, " +
                "l.loan_amount AS loanAmount, " +
                "l.start_date AS startDate, " +
                "l.end_date AS endDate, " +
                "COALESCE(SUM(ls.paid_amount), 0) AS totalPaidAmount, " +
                "COALESCE(ps.lastPayment, 0) AS lastPaidAmount, " +
                "COALESCE(SUM(ls.due_amount), 0) AS totalArrears, " +
                "(l.loan_amount - COALESCE(SUM(ls.paid_amount), 0)) AS totalOutstanding " +
                "FROM loan l " +
                "JOIN customers c ON l.customer_id = c.customer_id " +
                "LEFT JOIN loan_schedule ls ON l.loan_id = ls.loan_id " +
                "LEFT JOIN ( " +
                "   SELECT cusId, lastPayment " +
                "   FROM payment_summary " +
                "   WHERE (cusId, payment_date) IN ( " +
                "       SELECT cusId, MAX(payment_date) " +
                "       FROM payment_summary " +
                "       GROUP BY cusId " +
                "   ) " +
                ") ps ON c.customer_id = ps.cusId " +
                "WHERE c.customer_id = ? " +
                "GROUP BY l.loan_id, c.customer_id, ps.lastPayment, l.loan_no, l.loan_amount, l.start_date, l.end_date";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            LoanReportResponse response = new LoanReportResponse();
            response.setCustomerName(rs.getString("customerName"));
            response.setMobileNo(rs.getString("mobileNo"));
            response.setLoanNo(rs.getInt("loanNo"));
            response.setLoanAmount(rs.getDouble("loanAmount"));
            response.setStartDate(rs.getDate("startDate").toLocalDate());
            response.setEndDate(rs.getDate("endDate").toLocalDate());
            response.setTotalPaidAmount(rs.getDouble("totalPaidAmount"));
            response.setLastPaidAmount(rs.getDouble("lastPaidAmount"));
            response.setTotalArrears(rs.getDouble("totalArrears"));
            response.setTotalOutstanding(rs.getDouble("totalOutstanding"));
            return response;
        }, customerId);
    }


    public List<CustomerResponse> getAllCustomers() {
        String getAllCustomers = "SELECT * FROM customers";

        return jdbcTemplate.query(getAllCustomers, (rs, rowNum) -> {
            CustomerResponse customerResponse = new CustomerResponse();
            customerResponse.setCusId(rs.getInt("customer_id"));
            customerResponse.setCusName(rs.getString("full_name"));

            return customerResponse;
        });
    }
}