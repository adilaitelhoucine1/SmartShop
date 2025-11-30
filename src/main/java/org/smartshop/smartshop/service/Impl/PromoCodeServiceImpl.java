package org.smartshop.smartshop.service.Impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartshop.smartshop.DTO.promocode.PromoCodeCreateDTO;
import org.smartshop.smartshop.DTO.promocode.PromoCodeReadDTO;
import org.smartshop.smartshop.entity.PromoCode;
import org.smartshop.smartshop.exception.BusinessLogicException;
import org.smartshop.smartshop.exception.ResourceNotFoundException;
import org.smartshop.smartshop.mapper.PromoCodeMapper;
import org.smartshop.smartshop.repository.PromoCodeRepository;
import org.smartshop.smartshop.service.PromoCodeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PromoCodeServiceImpl implements PromoCodeService {

    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeMapper promoCodeMapper;

    public List<PromoCodeReadDTO> getAllPromoCodes(){
        return promoCodeRepository.findAll().stream().
                map(promoCodeMapper::toReadDTO).toList();
    }

    public PromoCodeReadDTO createPromoCode(@Valid PromoCodeCreateDTO promoCodeCreateDTO){
        PromoCode promoCode=PromoCode.builder()
                .code(promoCodeCreateDTO.getCode())
                .discountPercentage(promoCodeCreateDTO.getDiscountPercentage())
                .isActive(true)
                .build();
        promoCodeRepository.save(promoCode);
        return promoCodeMapper.toReadDTO(promoCode);
    }
    public  PromoCodeReadDTO getPromoById(Long id){
        PromoCodeReadDTO code=getAllPromoCodes().stream().filter(p->p.getId().equals(id))
                .findFirst().orElseThrow(()-> new ResourceNotFoundException("this promo doesn t exiite"));
        return code;
    }
    public  PromoCodeReadDTO desactiverPromo(Long id){
        PromoCodeReadDTO code=getAllPromoCodes().stream().filter(p->p.getId().equals(id))
                .findFirst().orElseThrow(()-> new ResourceNotFoundException("this promo doesn t exiite"));

        if (!code.getIsActive()){
            throw new BusinessLogicException("Already Disabled");
        }
        code.setIsActive(false);
        promoCodeRepository.save(promoCodeMapper.toEntity(code));
        return code;
    }
}
