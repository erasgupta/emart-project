package com.portal.emart.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.emart.api.entity.Customers;

@Repository
public interface UserRepository extends JpaRepository<Customers, Long> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Customers findByEmailOrPhone(String email, String phone);
}

