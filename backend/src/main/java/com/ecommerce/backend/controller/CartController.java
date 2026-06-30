package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.backend.entity.Cart;
import com.ecommerce.backend.repository.CartRepository;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/{userId}")
    public List<Cart> getCart(@PathVariable Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @PostMapping("/add")
    public Cart addToCart(@RequestBody Cart cart) {
        return cartRepository.save(cart);
    }

    @PutMapping("/update/{id}")
    public Cart updateQuantity(@PathVariable Long id, @RequestBody Cart updatedCart) {

        Cart cart = cartRepository.findById(id).orElseThrow();

        cart.setQuantity(updatedCart.getQuantity());

        return cartRepository.save(cart);
    }

    @DeleteMapping("/{id}")
    public void removeFromCart(@PathVariable Long id) {
        cartRepository.deleteById(id);
    }
}