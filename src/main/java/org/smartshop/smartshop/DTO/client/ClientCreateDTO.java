package org.smartshop.smartshop.DTO.client;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class ClientCreateDTO {
    @NotBlank(message = "Le nom est obligatoire")
    private String name;
    
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;
}