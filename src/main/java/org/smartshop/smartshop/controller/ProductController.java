package org.smartshop.smartshop.controller;


import org.smartshop.smartshop.DTO.product.ProductUpdateDTO;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.smartshop.smartshop.DTO.product.ProductCreateDTO;
import org.smartshop.smartshop.DTO.product.ProductReadDTO;
import org.smartshop.smartshop.service.ProductService;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private  final ProductService productService;

    public ProductController(ProductService productService){this.productService=productService;}

    @GetMapping
    public ResponseEntity<List<ProductReadDTO>> getallProducts(){
        List<ProductReadDTO> listProducts=productService.getallProducts();
        return ResponseEntity.ok(listProducts);
    }
    @GetMapping("/{id}")
     public  ResponseEntity<ProductReadDTO> getProductById(@PathVariable("id") Long id){
        ProductReadDTO product=productService.getProductById(id);
        return ResponseEntity.ok(product);
    }


    @PostMapping
    public ResponseEntity<ProductReadDTO> createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO){
        ProductReadDTO product =productService.createProduct(productCreateDTO);
        return ResponseEntity.ok(product);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductReadDTO> updateProduct(@Valid @RequestBody ProductUpdateDTO productUpdateDTO,
                                                        @PathVariable("id") Long id){
        ProductReadDTO  productUpdated=productService.updateProduct(id,productUpdateDTO);
        return ResponseEntity.ok(productUpdated);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok(Map.of("message","Item deleted succesffuly"));
    }

}
