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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final OrderItemRepository orderItemRepository;

    public List<ProductReadDTO> getallProducts(){
        return productRepository.findAll().stream().filter(product -> product.getIsDeleted()==false)
                .map(productMapper::toReadDTO).toList();
    }
    public ProductReadDTO createProduct(ProductCreateDTO productCreateDTO){
        Product product =Product.builder()
                .name(productCreateDTO.getName())
                .unitPrice(productCreateDTO.getUnitPrice())
                .availableStock(productCreateDTO.getAvailableStock())
                .build();
        productRepository.save(product);
        return productMapper.toReadDTO(product);
    }

    public  ProductReadDTO getProductById(Long id){

        return getallProducts().stream().filter(p->p.getId().equals(id)).
                findFirst().orElseThrow(()->new ResourceNotFoundException("product with this id  doesn t existe"));
    }

    public ProductReadDTO updateProduct(Long id , ProductUpdateDTO productUpdateDTO){
        Product product=productRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("product with this id  doesn t existe")
        );
        productMapper.updateEntity(productUpdateDTO,product);
        return productMapper.toReadDTO(product);
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
