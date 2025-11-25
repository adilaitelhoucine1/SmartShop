package org.smartshop.smartshop.mapper;

import org.smartshop.smartshop.DTO.order.OrderCreateDTO;
import org.smartshop.smartshop.DTO.order.OrderReadDTO;
import org.smartshop.smartshop.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    private final ProductMapper productMapper;

    public OrderItemMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public OrderItem toEntity(OrderCreateDTO.OrderItemCreateDTO dto) {
        return OrderItem.builder()
                .quantity(dto.getQuantity())
                .build();
    }

    public OrderReadDTO.OrderItemReadDTO toReadDTO(OrderItem entity) {
        OrderReadDTO.OrderItemReadDTO dto = new OrderReadDTO.OrderItemReadDTO();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPriceAtTime());
        dto.setTotalPrice(entity.getLineTotal());
        
        if (entity.getProduct() != null) {
            dto.setProduct(productMapper.toReadDTO(entity.getProduct()));
        }
        
        return dto;
    }
}