package org.smartshop.smartshop.DTO.promocode;

import lombok.Data;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import java.math.BigDecimal;

@Data
public class PromoCodeUpdateDTO {
    private String code;
    
    @DecimalMin(value = "0.01", message = "Le pourcentage doit être supérieur à 0")
    @DecimalMax(value = "100.00", message = "Le pourcentage ne peut pas dépasser 100")
    private BigDecimal discountPercentage;
    
    private Boolean isActive;
}