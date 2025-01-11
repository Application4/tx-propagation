package com.javatechie.service;

import com.javatechie.entity.AuditLog;
import com.javatechie.entity.Order;
import com.javatechie.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentValidatorService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    // Validate payment (runs in a nested transaction)
    @Transactional(propagation = Propagation.MANDATORY)
    public void validatePayment(Order order) {
        // Assume payment processing happens here
        boolean paymentSuccessful = false;

        // If payment is unsuccessful, we log the payment failure in the nested transaction
        if (!paymentSuccessful) {
            AuditLog paymentFailureLog = new AuditLog();
            paymentFailureLog.setOrderId(Long.valueOf(order.getId()));
            paymentFailureLog.setAction("Payment Failed for Order");
            paymentFailureLog.setTimestamp(LocalDateTime.now());

            // Save the payment failure log
            auditLogRepository.save(paymentFailureLog);
        }

        if(order.getTotalPrice()>25000){
            throw new RuntimeException("NESTED failure..");
        }
    }

}
