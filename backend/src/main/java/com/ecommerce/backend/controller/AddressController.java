package com.ecommerce.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> getUserAddresses(
            @PathVariable Long userId
    ) {

        List<Map<String, Object>> addresses = new ArrayList<>();

        Map<String, Object> address = new HashMap<>();

        address.put("id", 1);
        address.put("fullName", "Aditya Kandalkar");
        address.put("phone", "9876543210");
        address.put("addressLine", "Kolhapur Road");
        address.put("city", "Kolhapur");
        address.put("state", "Maharashtra");
        address.put("pincode", "416003");

        addresses.add(address);

        return addresses;
    }

    @PostMapping("/add")
    public Map<String, Object> addAddress(
            @RequestBody Map<String, Object> address
    ) {

        System.out.println("New Address Saved: " + address);

        return address;
    }
}
