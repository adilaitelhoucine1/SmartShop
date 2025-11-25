package org.smartshop.smartshop.DTO.payment;

import lombok.Data;
import org.smartshop.smartshop.enums.PaymentStatus;
import java.time.LocalDate;

@Data
public class PaymentUpdateDTO {
    private PaymentStatus status;
    private LocalDate dueDate;
    private String bankName;
}