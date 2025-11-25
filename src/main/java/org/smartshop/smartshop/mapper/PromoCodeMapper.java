package org.smartshop.smartshop.mapper;

import org.smartshop.smartshop.entity.PromoCode;
import org.springframework.stereotype.Component;

@Component
public class PromoCodeMapper {

    public PromoCode toEntity(String code, java.math.BigDecimal discountPercentage) {
        return PromoCode.builder()
                .code(code)
                .discountPercentage(discountPercentage)
                .build();
    }
}