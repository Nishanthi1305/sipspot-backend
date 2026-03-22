package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class BookingRequestDto {
    private Long userId;
    private Long cafeId;
    private Long tableId;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private Integer guests;
}