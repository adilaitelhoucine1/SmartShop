package org.smartshop.smartshop.DTO.promocode;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PromoCodeReadDTO {
    private Long id;
    private String code;
    private BigDecimal discountPercentage;
    private Boolean isActive;
}