package org.smartshop.smartshop.service.Impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.smartshop.smartshop.DTO.product.ProductCreateDTO;
import org.smartshop.smartshop.DTO.product.ProductReadDTO;
import org.smartshop.smartshop.DTO.product.ProductUpdateDTO;
import org.smartshop.smartshop.entity.Product;
import org.smartshop.smartshop.exception.ResourceNotFoundException;
import org.smartshop.smartshop.exception.UnauthorizedException;
import org.smartshop.smartshop.mapper.ProductMapper;
import org.smartshop.smartshop.repository.OrderItemRepository;
import org.smartshop.smartshop.repository.ProductRepository;
import org.smartshop.smartshop.service.ProductService;
import org.smartshop.smartshop.specification.ProductSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrderItemRepository orderItemRepository;

    public Page<ProductReadDTO> getAllProducts(boolean includeDeleted, Pageable pageable){
        Specification<Product> spec = includeDeleted ? null : ProductSpecification.isNotDeleted();
        return productRepository.findAll(spec, pageable).map(productMapper::toReadDTO);
    }

    public ProductReadDTO createProduct(ProductCreateDTO productCreateDTO){
        Product product =Product.builder()
                .name(productCreateDTO.getName())
                .unitPrice(productCreateDTO.getUnitPrice())
                .availableStock(productCreateDTO.getAvailableStock())
                .isDeleted(false)
                .build();
        productRepository.save(product);
        return productMapper.toReadDTO(product);
    }


    public ProductReadDTO getProductById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return productMapper.toReadDTO(product);
    }

    public ProductReadDTO updateProduct(Long id, ProductUpdateDTO productUpdateDTO){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productMapper.updateEntity(productUpdateDTO, product);
        return productMapper.toReadDTO(productRepository.save(product));
    }

    public  void deleteProduct(Long id){
        Product product = productRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException("product with this id  doesn t existe")
        );
        if (orderItemRepository.findAllByProduct(product).isEmpty()){
            product.setIsDeleted(true);
            productRepository.save(product);
        }
        productRepository.delete(product);

    }
}
