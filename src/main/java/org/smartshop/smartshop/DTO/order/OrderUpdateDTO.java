package org.smartshop.smartshop.DTO.order;

import lombok.Data;
import org.smartshop.smartshop.enums.OrderStatus;

@Data
public class OrderUpdateDTO {
    private OrderStatus status;
    private Long promoCodeId;
}