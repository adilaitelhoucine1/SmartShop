package org.smartshop.smartshop.mapper;

import org.smartshop.smartshop.DTO.client.ClientCreateDTO;
import org.smartshop.smartshop.DTO.client.ClientReadDTO;
import org.smartshop.smartshop.DTO.client.ClientUpdateDTO;
import org.smartshop.smartshop.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public Client toEntity(ClientCreateDTO dto) {
        return Client.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }

    public ClientReadDTO toReadDTO(Client entity) {
        ClientReadDTO dto = new ClientReadDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setEmail(entity.getEmail());
        dto.setLoyaltyTier(entity.getLoyaltyTier());
        dto.setTotalOrders(entity.getTotalOrders());
        dto.setTotalSpent(entity.getTotalSpent());
        dto.setFirstOrderDate(entity.getFirstOrderDate());
        dto.setLastOrderDate(entity.getLastOrderDate());
        return dto;
    }

    public void updateEntity(ClientUpdateDTO dto, Client entity) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }
        if (dto.getLoyaltyTier() != null) {
            entity.setLoyaltyTier(dto.getLoyaltyTier());
        }
    }
}