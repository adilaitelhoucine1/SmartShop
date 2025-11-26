package org.smartshop.smartshop.DTO.product;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductReadDTO {
    private Long id;
    private String name;
    private BigDecimal unitPrice;
    private Integer availableStock;
   private Boolean isDeleted = false;
}