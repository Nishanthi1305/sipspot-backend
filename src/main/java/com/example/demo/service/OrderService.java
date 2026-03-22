package com.example.demo.service;

import com.example.demo.dto.OrderRequestDto;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired private OrderRepository orderRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CafeRepository cafeRepository;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private MenuItemRepository menuItemRepository;

    // ── Place Order ──────────────────────────────────────────────────
    public Map<String, Object> placeOrder(OrderRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found!"));

        Cafe cafe = cafeRepository.findById(dto.getCafeId())
            .orElseThrow(() -> new RuntimeException("Café not found!"));

        Order order = new Order();
        order.setUser(user);
        order.setCafe(cafe);
        order.setStatus("PLACED");
        order.setPaymentStatus("PENDING");

        // Link booking if provided
        if (dto.getBookingId() != null) {
            Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found!"));
            order.setBooking(booking);
        }

        // Build order items
        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (OrderRequestDto.OrderItemDto itemDto : dto.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setItemName(itemDto.getItemName());
            orderItem.setItemPrice(itemDto.getItemPrice());
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setSubtotal(itemDto.getItemPrice() * itemDto.getQuantity());

            if (itemDto.getMenuItemId() != null) {
                menuItemRepository.findById(itemDto.getMenuItemId())
                    .ifPresent(orderItem::setMenuItem);
            }

            total += orderItem.getSubtotal();
            orderItems.add(orderItem);
        }

        order.setTotalAmount(total);
        order.setOrderItems(orderItems);
        orderRepository.save(order);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order placed successfully!");
        response.put("orderId", order.getId());
        response.put("total", total);
        response.put("status", order.getStatus());
        return response;
    }

    // ── Get My Orders (Customer) ─────────────────────────────────────
    public List<Order> getMyOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // ── Get Café Orders (Owner/Chef) ─────────────────────────────────
    public List<Order> getCafeOrders(Long cafeId) {
        return orderRepository.findByCafeId(cafeId);
    }

    // ── Update Order Status (Chef/Waiter) ────────────────────────────
    public Map<String, Object> updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found!"));

        // Validate status transition
        Map<String, String> validTransitions = Map.of(
            "PLACED", "PREPARING",
            "PREPARING", "READY",
            "READY", "SERVED"
        );

        String currentStatus = order.getStatus();
        if (!validTransitions.containsKey(currentStatus) ||
            !validTransitions.get(currentStatus).equals(status)) {
            throw new RuntimeException("Invalid status transition: " + currentStatus + " → " + status);
        }

        order.setStatus(status);
        orderRepository.save(order);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Order status updated to " + status);
        response.put("orderId", orderId);
        response.put("status", status);
        return response;
    }

    // ── Update Payment Status ────────────────────────────────────────
    public void markOrderPaid(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found!"));
        order.setPaymentStatus("PAID");
        orderRepository.save(order);
    }
}
