package com.ecommerce.backend.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.backend.dto.UserProfileDto;
import com.ecommerce.backend.entity.User;
import com.ecommerce.backend.repository.OrderRepository;
import com.ecommerce.backend.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    public UserProfileDto getProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));

        long orders = orderRepository.countByUser(user);
        int wishlist = 0;
        int cart = 0;

        String avatarSeed = URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);
        String avatar = "https://api.dicebear.com/6.x/thumbs/svg?seed=" + avatarSeed;

        return new UserProfileDto(
                user.getName(),
                user.getEmail(),
                avatar,
                orders,
                wishlist,
                cart
        );
    }
}
