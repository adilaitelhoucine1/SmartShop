package org.smartshop.smartshop.DTO.client;

import lombok.Data;
import org.smartshop.smartshop.enums.CustomerTier;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ClientProfileDTO {
    private String name;
    private String email;
    private CustomerTier loyaltyTier;
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;
}