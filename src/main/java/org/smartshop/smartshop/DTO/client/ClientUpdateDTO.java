package org.smartshop.smartshop.DTO.client;

import lombok.Data;
import jakarta.validation.constraints.Email;
import org.smartshop.smartshop.enums.CustomerTier;

@Data
public class ClientUpdateDTO {
    private String name;
    
    @Email(message = "Format d'email invalide")
    private String email;
    
    private CustomerTier loyaltyTier;
}