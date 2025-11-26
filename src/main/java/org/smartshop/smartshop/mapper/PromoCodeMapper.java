package org.smartshop.smartshop.mapper;

import org.mapstruct.*;
import org.smartshop.smartshop.DTO.promocode.PromoCodeCreateDTO;
import org.smartshop.smartshop.DTO.promocode.PromoCodeReadDTO;
import org.smartshop.smartshop.DTO.promocode.PromoCodeUpdateDTO;
import org.smartshop.smartshop.entity.PromoCode;

@Mapper(componentModel = "spring")
public interface PromoCodeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "orders", ignore = true)
    PromoCode toEntity(PromoCodeCreateDTO dto);

    PromoCodeReadDTO toReadDTO(PromoCode entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(PromoCodeUpdateDTO dto, @MappingTarget PromoCode entity);
}