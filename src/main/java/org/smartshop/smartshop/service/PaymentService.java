package org.smartshop.smartshop.service;

import jakarta.validation.Valid;
import org.smartshop.smartshop.DTO.payment.PaymentCreateDTO;
import org.smartshop.smartshop.DTO.payment.PaymentReadDTO;

public interface PaymentService {
    PaymentReadDTO createPayment(@Valid PaymentCreateDTO paymentCreateDTO);

    PaymentReadDTO validatePayment(Long id);

    PaymentReadDTO rejectPayment(Long paymentId);
}
