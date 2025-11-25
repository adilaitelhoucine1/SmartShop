package org.smartshop.smartshop.mapper;

import org.smartshop.smartshop.DTO.product.ProductCreateDTO;
import org.smartshop.smartshop.DTO.product.ProductReadDTO;
import org.smartshop.smartshop.DTO.product.ProductUpdateDTO;
import org.smartshop.smartshop.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductCreateDTO dto) {
        return Product.builder()
                .name(dto.getName())
                .unitPrice(dto.getUnitPrice())
                .availableStock(dto.getAvailableStock())
                .build();
    }

    public ProductReadDTO toReadDTO(Product entity) {
        ProductReadDTO dto = new ProductReadDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setUnitPrice(entity.getUnitPrice());
        dto.setAvailableStock(entity.getAvailableStock());
        dto.setIsDeleted(entity.getIsDeleted());
        return dto;
    }

    public void updateEntity(ProductUpdateDTO dto, Product entity) {
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getUnitPrice() != null) {
            entity.setUnitPrice(dto.getUnitPrice());
        }
        if (dto.getAvailableStock() != null) {
            entity.setAvailableStock(dto.getAvailableStock());
        }
    }
}