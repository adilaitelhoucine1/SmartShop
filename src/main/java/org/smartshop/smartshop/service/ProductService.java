package org.smartshop.smartshop.service;

import jakarta.validation.Valid;
import org.smartshop.smartshop.DTO.product.ProductCreateDTO;
import org.smartshop.smartshop.DTO.product.ProductReadDTO;
import org.smartshop.smartshop.DTO.product.ProductUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Page<ProductReadDTO> getAllProducts(boolean includeDeleted, Pageable pageable);

    ProductReadDTO createProduct(@Valid ProductCreateDTO productCreateDTO);

    ProductReadDTO updateProduct(Long id,ProductUpdateDTO productUpdateDTO);

    void deleteProduct(Long id);

    ProductReadDTO getProductById(Long id);
}
