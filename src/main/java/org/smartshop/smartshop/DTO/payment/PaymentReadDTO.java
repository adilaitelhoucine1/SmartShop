package org.smartshop.smartshop.DTO.payment;

import lombok.Data;
import org.smartshop.smartshop.enums.PaymentStatus;
import org.smartshop.smartshop.enums.PaymentType;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentReadDTO {
    private Long id;
    private BigDecimal amount;
    private PaymentType paymentType;
    private PaymentStatus status;
    private String reference;
    private LocalDate dueDate;
    private String bankName;
    private Long orderId;
}