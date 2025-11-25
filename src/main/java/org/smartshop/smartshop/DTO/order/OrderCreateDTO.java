package org.smartshop.smartshop.DTO.order;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderCreateDTO {
    @NotNull(message = "L'ID du client est obligatoire")
    private Long clientId;
    
    private Long promoCodeId;
    
    @NotNull(message = "Les articles de commande sont obligatoires")
    private List<OrderItemCreateDTO> orderItems;
    
    @Data
    public static class OrderItemCreateDTO {
        @NotNull(message = "L'ID du produit est obligatoire")
        private Long productId;
        
        @NotNull(message = "La quantité est obligatoire")
        private Integer quantity;
    }
}