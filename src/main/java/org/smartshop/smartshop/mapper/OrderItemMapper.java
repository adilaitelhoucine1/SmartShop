package org.smartshop.smartshop.mapper;

import org.mapstruct.*;
import org.smartshop.smartshop.DTO.order.OrderCreateDTO;
import org.smartshop.smartshop.DTO.order.OrderReadDTO;
import org.smartshop.smartshop.entity.OrderItem;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface OrderItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unitPriceAtTime", ignore = true)
    @Mapping(target = "lineTotal", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderItem toEntity(OrderCreateDTO.OrderItemCreateDTO dto);

    @Mapping(source = "unitPriceAtTime", target = "unitPrice")
    @Mapping(source = "lineTotal", target = "totalPrice")
    OrderReadDTO.OrderItemReadDTO toReadDTO(OrderItem entity);
}