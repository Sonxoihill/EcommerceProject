package com.example.ecommerceproject.repository;

import com.example.ecommerceproject.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
