package org.smartshop.smartshop.mapper;

import org.smartshop.smartshop.DTO.payment.PaymentCreateDTO;
import org.smartshop.smartshop.DTO.payment.PaymentReadDTO;
import org.smartshop.smartshop.DTO.payment.PaymentUpdateDTO;
import org.smartshop.smartshop.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentCreateDTO dto) {
        return Payment.builder()
                .amount(dto.getAmount())
                .paymentType(dto.getPaymentType())
                .reference(dto.getReference())
                .dueDate(dto.getDueDate())
                .bankName(dto.getBankName())
                .build();
    }

    public PaymentReadDTO toReadDTO(Payment entity) {
        PaymentReadDTO dto = new PaymentReadDTO();
        dto.setId(entity.getId());
        dto.setAmount(entity.getAmount());
        dto.setPaymentType(entity.getPaymentType());
        dto.setStatus(entity.getStatus());
        dto.setReference(entity.getReference());
        dto.setDueDate(entity.getDueDate());
        dto.setBankName(entity.getBankName());
        
        if (entity.getOrder() != null) {
            dto.setOrderId(entity.getOrder().getId());
        }
        
        return dto;
    }

    public void updateEntity(PaymentUpdateDTO dto, Payment entity) {
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
        if (dto.getDueDate() != null) {
            entity.setDueDate(dto.getDueDate());
        }
        if (dto.getBankName() != null) {
            entity.setBankName(dto.getBankName());
        }
    }
}