package org.smartshop.smartshop.mapper;

import org.mapstruct.*;
import org.smartshop.smartshop.DTO.order.OrderCreateDTO;
import org.smartshop.smartshop.DTO.order.OrderReadDTO;
import org.smartshop.smartshop.DTO.order.OrderUpdateDTO;
import org.smartshop.smartshop.entity.Order;

@Mapper(componentModel = "spring", uses = {ClientMapper.class, OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "promoCode", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "payments", ignore = true)
    Order toEntity(OrderCreateDTO dto);

    @Mapping(source = "promoCode.code", target = "promoCodeName")
    OrderReadDTO toReadDTO(Order entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(OrderUpdateDTO dto, @MappingTarget Order entity);
}