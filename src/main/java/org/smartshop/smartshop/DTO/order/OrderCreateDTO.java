package org.smartshop.smartshop.DTO.order;

import jakarta.validation.constraints.Min;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import org.smartshop.smartshop.entity.Order;
import org.smartshop.smartshop.entity.Product;

import java.math.BigDecimal;
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
        private Product product;

        @NotNull(message = "La quantité est obligatoire")
        private Integer quantity;
        @NotNull(message = "Le prix unitaire est obligatoire")
        @Min(0)
        private BigDecimal unitPriceAtTime;



    }
}