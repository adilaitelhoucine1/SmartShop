package org.smartshop.smartshop.DTO.promocode;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PromoCodeCreateDTO {
    @NotBlank(message = "Le code promo est obligatoire")
    @Pattern(regexp = "PROMO-[A-Z0-9]{4}")
    private String code;
    
    @NotNull(message = "Le pourcentage de réduction est obligatoire")
    @DecimalMin(value = "0.01", message = "Le pourcentage doit être supérieur à 0")
    @DecimalMax(value = "100.00", message = "Le pourcentage ne peut pas dépasser 100")
    private BigDecimal discountPercentage;
}