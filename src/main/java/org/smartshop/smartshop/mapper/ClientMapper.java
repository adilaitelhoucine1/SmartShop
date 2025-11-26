package org.smartshop.smartshop.mapper;

import org.mapstruct.*;
import org.smartshop.smartshop.DTO.client.ClientCreateDTO;
import org.smartshop.smartshop.DTO.client.ClientReadDTO;
import org.smartshop.smartshop.DTO.client.ClientUpdateDTO;
import org.smartshop.smartshop.entity.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientCreateDTO dto);

    ClientReadDTO toReadDTO(Client entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ClientUpdateDTO dto, @MappingTarget Client entity);
}