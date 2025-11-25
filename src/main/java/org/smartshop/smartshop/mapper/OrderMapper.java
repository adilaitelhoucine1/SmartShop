package org.smartshop.smartshop.mapper;

import org.smartshop.smartshop.DTO.order.OrderCreateDTO;
import org.smartshop.smartshop.DTO.order.OrderReadDTO;
import org.smartshop.smartshop.DTO.order.OrderUpdateDTO;
import org.smartshop.smartshop.entity.Order;
import org.smartshop.smartshop.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final ClientMapper clientMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderMapper(ClientMapper clientMapper, OrderItemMapper orderItemMapper) {
        this.clientMapper = clientMapper;
        this.orderItemMapper = orderItemMapper;
    }

    public Order toEntity(OrderCreateDTO dto) {
        return Order.builder()
                .build();
    }

    public OrderReadDTO toReadDTO(Order entity) {
        OrderReadDTO dto = new OrderReadDTO();
        dto.setId(entity.getId());
        dto.setStatus(entity.getStatus());
        dto.setSubTotalHT(entity.getSubTotalHT());
        dto.setTotalDiscount(entity.getTotalDiscount());
        dto.setGrandTotalTTC(entity.getGrandTotalTTC());
        dto.setRemainingAmount(entity.getRemainingAmount());
        
        if (entity.getClient() != null) {
            dto.setClient(clientMapper.toReadDTO(entity.getClient()));
        }
        
        if (entity.getPromoCode() != null) {
            dto.setPromoCodeName(entity.getPromoCode().getCode());
        }
        
        if (entity.getOrderItems() != null) {
            dto.setOrderItems(entity.getOrderItems().stream()
                    .map(orderItemMapper::toReadDTO)
                    .collect(Collectors.toList()));
        }
        
        return dto;
    }

    public void updateEntity(OrderUpdateDTO dto, Order entity) {
        if (dto.getStatus() != null) {
            entity.setStatus(dto.getStatus());
        }
    }
}