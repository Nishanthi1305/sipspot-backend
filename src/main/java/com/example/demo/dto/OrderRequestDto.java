package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDto {
    private Long userId;
    private Long cafeId;
    private Long bookingId;
    private List<OrderItemDto> items;

    @Data
    public static class OrderItemDto {
        private Long menuItemId;
        private String itemName;
        private Double itemPrice;
        private Integer quantity;
    }
}
