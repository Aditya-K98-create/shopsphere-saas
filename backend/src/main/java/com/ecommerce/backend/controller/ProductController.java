package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(value = "category", required = false) String category) {

        if (category != null
                && !category.trim().isEmpty()
                && !category.equalsIgnoreCase("all")
                && !category.equalsIgnoreCase("all collection")
                && !category.equalsIgnoreCase("our all")) {

            return ResponseEntity.ok(
                    productRepository.findByCategoryIgnoreCase(category.trim()));
        }

        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {

        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {

        if (product.getImages() == null) {
            product.setImages(List.of());
        }

        Product savedProduct = productRepository.save(product);

        return ResponseEntity.ok(savedProduct);
    }

    
    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product updatedProduct) {

        return productRepository.findById(id)
                .map(existingProduct -> {

                    existingProduct.setName(updatedProduct.getName());
                    existingProduct.setDescription(updatedProduct.getDescription());
                    existingProduct.setPrice(updatedProduct.getPrice());

                    existingProduct.setCategory(updatedProduct.getCategory());
                    existingProduct.setRating(updatedProduct.getRating());
                    existingProduct.setTag(updatedProduct.getTag());

                    
                    existingProduct.setMainImage(updatedProduct.getMainImage());

                  
                    existingProduct.setImages(updatedProduct.getImages());

                    Product savedProduct = productRepository.save(existingProduct);

                    return ResponseEntity.ok(savedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {

        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);

                    return ResponseEntity.ok()
                            .body("{\"message\":\"Product deleted successfully!\"}");
                })
                .orElse(ResponseEntity.notFound().build());
    }
}