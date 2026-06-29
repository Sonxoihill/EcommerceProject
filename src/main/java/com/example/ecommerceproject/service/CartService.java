package com.example.ecommerceproject.service;

import com.example.ecommerceproject.entity.CartEntity;
import com.example.ecommerceproject.entity.CartItemEntity;
import com.example.ecommerceproject.entity.ProductEntity;
import com.example.ecommerceproject.entity.UserEntity;
import com.example.ecommerceproject.repository.CartItemRepository;
import com.example.ecommerceproject.repository.CartRepository;
import com.example.ecommerceproject.repository.ProductRepository;
import com.example.ecommerceproject.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    public CartItemEntity addProductToCart(Long userId, Long productId, int quantity) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("San pham khong ton tai"));

        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserEntity user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Nguoi dung khong ton tai"));
                    CartEntity newCart = new CartEntity();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        Optional<CartItemEntity>  cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if(cartItem.isPresent()) {
            CartItemEntity cartItemEntity = cartItem.get();
            cartItemEntity.setQuantity(cartItem.get().getQuantity() + quantity);
            return cartItemRepository.save(cartItemEntity);
        }else{
            CartItemEntity cartItemEntity = new CartItemEntity();
            cartItemEntity.setQuantity(quantity);
            cartItemEntity.setProduct(product);
            cartItemEntity.setCart(cart);
            return cartItemRepository.save(cartItemEntity);
        }
    }
}
