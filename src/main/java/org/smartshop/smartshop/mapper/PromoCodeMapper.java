package org.smartshop.smartshop.mapper;

import org.mapstruct.*;
import org.smartshop.smartshop.entity.PromoCode;

@Mapper(componentModel = "spring")
public interface PromoCodeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "orders", ignore = true)
    PromoCode toEntity(String code, java.math.BigDecimal discountPercentage);
}