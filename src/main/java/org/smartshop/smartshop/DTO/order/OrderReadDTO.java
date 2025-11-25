package org.smartshop.smartshop.DTO.order;

import lombok.Data;
import org.smartshop.smartshop.enums.OrderStatus;
import org.smartshop.smartshop.DTO.client.ClientReadDTO;
import org.smartshop.smartshop.DTO.product.ProductReadDTO;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderReadDTO {
    private Long id;
    private OrderStatus status;
    private BigDecimal subTotalHT;
    private BigDecimal totalDiscount;
    private BigDecimal grandTotalTTC;
    private BigDecimal remainingAmount;
    private ClientReadDTO client;
    private String promoCodeName;
    private List<OrderItemReadDTO> orderItems;
    
    @Data
    public static class OrderItemReadDTO {
        private Long id;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal totalPrice;
        private ProductReadDTO product;
    }
}