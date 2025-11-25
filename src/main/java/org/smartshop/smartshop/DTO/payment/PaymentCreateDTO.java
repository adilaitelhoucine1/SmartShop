package org.smartshop.smartshop.DTO.payment;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import org.smartshop.smartshop.enums.PaymentType;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentCreateDTO {
    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le montant doit être positif")
    private BigDecimal amount;
    
    @NotNull(message = "Le type de paiement est obligatoire")
    private PaymentType paymentType;
    
    @NotBlank(message = "La référence est obligatoire")
    private String reference;
    
    @NotNull(message = "L'ID de la commande est obligatoire")
    private Long orderId;
    
    private LocalDate dueDate;
    private String bankName;
}