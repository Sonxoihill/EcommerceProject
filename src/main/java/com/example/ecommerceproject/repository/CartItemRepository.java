package com.example.ecommerceproject.repository;

import com.example.ecommerceproject.entity.CartEntity;
import com.example.ecommerceproject.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    Optional<CartItemEntity> findByCartIdAndProductId(Long cartId, Long productId);
    List<CartItemEntity> findByCartId(Long cartId);

    @Modifying
    @Transactional
    void deleteByCartId(Long cartId);
}
