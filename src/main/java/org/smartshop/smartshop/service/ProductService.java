package org.smartshop.smartshop.service;

import jakarta.validation.Valid;
import org.smartshop.smartshop.DTO.product.ProductCreateDTO;
import org.smartshop.smartshop.DTO.product.ProductReadDTO;
import org.smartshop.smartshop.DTO.product.ProductUpdateDTO;

import java.util.List;

public interface ProductService {

    List<ProductReadDTO> getallProducts();

    ProductReadDTO createProduct(@Valid ProductCreateDTO productCreateDTO);

    ProductReadDTO updateProduct(Long id,ProductUpdateDTO productUpdateDTO);

    void deleteProduct(Long id);

    ProductReadDTO getProductById(Long id);
}
