package org.smartshop.smartshop.DTO.product;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

@Data
public class ProductCreateDTO {
    @NotBlank(message = "Le nom du produit est obligatoire")
    private String name;
    
    @NotNull(message = "Le prix unitaire est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
    private BigDecimal unitPrice;
    
    @NotNull(message = "Le stock disponible est obligatoire")
    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    private Integer availableStock;
}