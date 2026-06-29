package com.example.ecommerceproject.controller;

import com.example.ecommerceproject.entity.CartItemEntity;
import com.example.ecommerceproject.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public CartItemEntity addToCart(@RequestParam Long userId,
                                    @RequestParam Long productId,
                                    @RequestParam int quantity){
        return cartService.addProductToCart(userId, productId, quantity);
    }
}
