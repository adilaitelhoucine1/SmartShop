package org.smartshop.smartshop.controller;


import jakarta.validation.Valid;
import org.smartshop.smartshop.DTO.promocode.PromoCodeCreateDTO;
import org.smartshop.smartshop.DTO.promocode.PromoCodeReadDTO;
import org.smartshop.smartshop.service.PromoCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promocode")
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    public PromoCodeController(PromoCodeService promoCodeService){this.promoCodeService = promoCodeService;}

    @GetMapping
    public ResponseEntity<List<PromoCodeReadDTO>> getAllPromoCodes(){
        List<PromoCodeReadDTO> promolists=promoCodeService.getAllPromoCodes();
        return ResponseEntity.ok(promolists);
    }

    @PostMapping
    public ResponseEntity<PromoCodeReadDTO> createPromoCode(@Valid @RequestBody PromoCodeCreateDTO promoCodeCreateDTO){
        PromoCodeReadDTO promocode=promoCodeService.createPromoCode(promoCodeCreateDTO);
        return ResponseEntity.ok(promocode);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PromoCodeReadDTO> getPromCodeById(@PathVariable("id") Long id){
        PromoCodeReadDTO promocode=promoCodeService.getPromoById(id);
        return ResponseEntity.ok(promocode);
    }

    @GetMapping("/{id}/desactiver")
    public ResponseEntity<PromoCodeReadDTO> desactiverPromo(@PathVariable("id") Long id){

        PromoCodeReadDTO promo=promoCodeService.desactiverPromo(id);
        return ResponseEntity.ok(promo);
    }
}
