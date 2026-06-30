package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.entity.Wishlist;
import com.ecommerce.backend.repository.WishlistRepository;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin("*")
public class WishlistController {

    @Autowired
    private WishlistRepository wishlistRepository;

    
    @GetMapping("/{userId}")
    public List<Wishlist> getWishlist(@PathVariable Long userId) {
        return wishlistRepository.findByUserId(userId);
    }

    
    @PostMapping("/add")
    public Wishlist addWishlist(@RequestBody Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    
    @DeleteMapping("/{id}")
    public void removeWishlist(@PathVariable Long id) {
        wishlistRepository.deleteById(id);
    }
}