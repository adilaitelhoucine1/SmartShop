package org.smartshop.smartshop.DTO.product;

import lombok.Data;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

@Data
public class ProductUpdateDTO {
    private String name;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
    private BigDecimal unitPrice;
    
    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    private Integer availableStock;
}