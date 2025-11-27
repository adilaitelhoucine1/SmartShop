package org.smartshop.smartshop.service;

import jakarta.validation.Valid;
import org.smartshop.smartshop.DTO.promocode.PromoCodeCreateDTO;
import org.smartshop.smartshop.DTO.promocode.PromoCodeReadDTO;

import java.util.List;

public interface PromoCodeService {
    List<PromoCodeReadDTO> getAllPromoCodes();

    PromoCodeReadDTO createPromoCode(@Valid PromoCodeCreateDTO promoCodeCreateDTO);

    PromoCodeReadDTO getPromoById(Long id);

    PromoCodeReadDTO desactiverPromo(Long id);
}
