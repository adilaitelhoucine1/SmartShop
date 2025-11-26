package org.smartshop.smartshop.mapper;

import org.mapstruct.*;
import org.smartshop.smartshop.DTO.payment.PaymentCreateDTO;
import org.smartshop.smartshop.DTO.payment.PaymentReadDTO;
import org.smartshop.smartshop.DTO.payment.PaymentUpdateDTO;
import org.smartshop.smartshop.entity.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "order", ignore = true)
    Payment toEntity(PaymentCreateDTO dto);

    @Mapping(source = "order.id", target = "orderId")
    PaymentReadDTO toReadDTO(Payment entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(PaymentUpdateDTO dto, @MappingTarget Payment entity);
}