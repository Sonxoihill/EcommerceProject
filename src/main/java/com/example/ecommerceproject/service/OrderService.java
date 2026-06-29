package com.example.ecommerceproject.service;

import com.example.ecommerceproject.entity.*;
import com.example.ecommerceproject.repository.*;
import jakarta.transaction.Transactional;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Transactional
    public OrderEntity placeOrder(Long userId){
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Nguoi dung chua co gio hang"));

        List<CartItemEntity> cartItems = cartItemRepository.findByCartId(cart.getId());
        if(cartItems.isEmpty()){
            throw new RuntimeException("Gio hang dang trong, khong the dat hang");
        }

        OrderEntity order = new OrderEntity();
        order.setUser(cart.getUser());
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        double totalPrice = 0;
        List<OrderItemEntity> orderItemsToSave = new ArrayList<>();
        for (CartItemEntity cartItem : cartItems) {
            ProductEntity product = cartItem.getProduct();

            if(product.getStockQuantity() < cartItem.getQuantity()){
                throw new RuntimeException("Kho khong du hang hoac da het");
            }

            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            totalPrice += product.getPrice() *  cartItem.getQuantity();

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setProduct(product);
            orderItem.setOrder(order);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItemsToSave.add(orderItem);
        }
        order.setTotalPrice(totalPrice);
        OrderEntity savedOrder = orderRepository.save(order);

        for(OrderItemEntity item : orderItemsToSave){
            item.setOrder(savedOrder);
        }
        orderItemRepository.saveAll(orderItemsToSave);

        cartItemRepository.deleteByCartId(cart.getId());

        return savedOrder;
    }

    @Transactional
    public OrderEntity cancelOrder(Long orderId){
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Khong tim thay don hang"));

        if(!"PENDING".equals(order.getStatus())){
            throw new RuntimeException("Don hang da duoc giao hoac dang giao");
        }

        List<OrderItemEntity> orderItems = orderItemRepository.findByOrderId(orderId);
        for(OrderItemEntity item : orderItems){
            ProductEntity product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus("CANCELLED");

        return orderRepository.save(order);
    }
}
