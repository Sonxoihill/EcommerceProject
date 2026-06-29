package com.example.ecommerceproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "carts")
@Data
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;
}
