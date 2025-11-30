package org.smartshop.smartshop.service.Impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartshop.smartshop.DTO.payment.PaymentCreateDTO;
import org.smartshop.smartshop.DTO.payment.PaymentReadDTO;
import org.smartshop.smartshop.entity.Order;
import org.smartshop.smartshop.entity.Payment;
import org.smartshop.smartshop.enums.PaymentStatus;
import org.smartshop.smartshop.enums.PaymentType;
import org.smartshop.smartshop.exception.BusinessLogicException;
import org.smartshop.smartshop.exception.ResourceNotFoundException;
import org.smartshop.smartshop.mapper.PaymentMapper;
import org.smartshop.smartshop.repository.OrderRepository;
import org.smartshop.smartshop.repository.PaymentRepository;
import org.smartshop.smartshop.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;

    public PaymentReadDTO createPayment(@Valid PaymentCreateDTO paymentDTO) {
        Order order = orderRepository.findById(paymentDTO.getOrderId())
            .orElseThrow(() -> new ResourceNotFoundException("Order Not Found"));
        
        if (order.getRemainingAmount().equals(BigDecimal.ZERO)) {
            throw new BusinessLogicException("Order Already Paid");
        }

        if (paymentDTO.getPaymentType() == PaymentType.ESPECES 
            && paymentDTO.getAmount().compareTo(new BigDecimal("20000")) > 0) {
            throw new BusinessLogicException("Reached Limit 20 000 Dh");
        }

        Payment payment = Payment.builder()
            .amount(paymentDTO.getAmount())
            .paymentType(paymentDTO.getPaymentType())
            .reference(paymentDTO.getReference())
            .order(order)
            .build();

        if (paymentDTO.getPaymentType() == PaymentType.ESPECES) {
            payment.setStatus(PaymentStatus.ENCAISSE);
        } else {
            payment.setStatus(PaymentStatus.EN_ATTENTE);
            payment.setBankName(paymentDTO.getBankName());
            if (paymentDTO.getPaymentType() == PaymentType.CHEQUE) {
                payment.setDueDate(paymentDTO.getDueDate());
            }
        }

        paymentRepository.save(payment);

        if (paymentDTO.getPaymentType() == PaymentType.ESPECES) {
            order.setRemainingAmount(order.getRemainingAmount().subtract(payment.getAmount()));
            orderRepository.save(order);
        }

        return paymentMapper.toReadDTO(payment);
    }

    public PaymentReadDTO validatePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment Not Found"));

        if (payment.getStatus() != PaymentStatus.EN_ATTENTE) {
            throw new BusinessLogicException("Payment already processed");
        }

        Order order = payment.getOrder();
        if (order.getRemainingAmount().compareTo(payment.getAmount()) < 0) {
            throw new BusinessLogicException("Payment amount exceeds remaining order amount");
        }

        payment.setStatus(PaymentStatus.ENCAISSE);
        order.setRemainingAmount(order.getRemainingAmount().subtract(payment.getAmount()));
        
        paymentRepository.save(payment);
        orderRepository.save(order);
        
        return paymentMapper.toReadDTO(payment);
    }

    public PaymentReadDTO rejectPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment Not Found"));
        
        payment.setStatus(PaymentStatus.REJETE);
        paymentRepository.save(payment);
        
        return paymentMapper.toReadDTO(payment);
    }
}