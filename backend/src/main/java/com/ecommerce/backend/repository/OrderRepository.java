package com.ecommerce.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.backend.entity.Order;
import com.ecommerce.backend.entity.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    long countByStatus(String status);

    long countByUser(User user);

    List<Order> findByUser(User user);

    @Query("SELECT COALESCE(SUM(o.amount),0) FROM Order o")
    double calculateTotalRevenue();

}