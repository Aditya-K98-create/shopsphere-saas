package com.ecommerce.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.entity.Address;
import com.ecommerce.backend.repository.AddressRepository;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin("*")
public class AddressController {

    private final AddressRepository addressRepository;

    public AddressController(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    // Get all addresses
    @GetMapping
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    // Get addresses by userId
    @GetMapping("/user/{userId}")
    public List<Address> getAddressesByUser(@PathVariable Long userId) {
        return addressRepository.findByUserId(userId);
    }

    // Add new address
    @PostMapping
    public Address addAddress(@RequestBody Address address) {
        return addressRepository.save(address);
    }

    // Delete address
    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressRepository.deleteById(id);
    }
}