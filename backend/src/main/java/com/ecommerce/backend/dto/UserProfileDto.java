package com.ecommerce.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private String name;
    private String email;
    private String avatar;
    private long orders;
    private int wishlist;
    private int cart;
}
