package org.smartshop.smartshop.controller;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.smartshop.smartshop.DTO.promocode.PromoCodeCreateDTO;
import org.smartshop.smartshop.DTO.promocode.PromoCodeReadDTO;
import org.smartshop.smartshop.service.PromoCodeService;
import org.smartshop.smartshop.utils.SecurityAuth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promocode")
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    public PromoCodeController(PromoCodeService promoCodeService){this.promoCodeService = promoCodeService;}

    @GetMapping
    public ResponseEntity<List<PromoCodeReadDTO>> getAllPromoCodes( HttpSession session){
        SecurityAuth.requireAdmin(session);
        List<PromoCodeReadDTO> promolists=promoCodeService.getAllPromoCodes();
        return ResponseEntity.ok(promolists);
    }

    @PostMapping
    public ResponseEntity<PromoCodeReadDTO> createPromoCode(@Valid @RequestBody PromoCodeCreateDTO promoCodeCreateDTO,
                                                            HttpSession session){
        SecurityAuth.requireAdmin(session);
        PromoCodeReadDTO promocode=promoCodeService.createPromoCode(promoCodeCreateDTO);
        return ResponseEntity.ok(promocode);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromoCodeReadDTO> getPromCodeById(@PathVariable("id") Long id,
                                                            HttpSession session){
        SecurityAuth.requireAdmin(session);
        PromoCodeReadDTO promocode=promoCodeService.getPromoById(id);
        return ResponseEntity.ok(promocode);
    }

    @GetMapping("/{id}/desactiver")
    public ResponseEntity<PromoCodeReadDTO> desactiverPromo(@PathVariable("id") Long id,
                                                            HttpSession session){
        SecurityAuth.requireAdmin(session);
        PromoCodeReadDTO promo=promoCodeService.desactiverPromo(id);
        return ResponseEntity.ok(promo);
    }
}
