package com.example.ecommerceproject.controller;

import com.example.ecommerceproject.entity.OrderEntity;
import com.example.ecommerceproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public OrderEntity checkout(@RequestParam Long userId){
        return orderService.placeOrder(userId);
    }
}
