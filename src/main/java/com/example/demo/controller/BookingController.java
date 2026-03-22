package com.example.demo.controller;

import com.example.demo.dto.BookingRequestDto;
import com.example.demo.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/booking")
@CrossOrigin(origins = "http://localhost:5173")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Customer creates booking
    @PostMapping("/create")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDto dto) {
        return ResponseEntity.ok(bookingService.createBooking(dto));
    }

    // Customer gets their bookings
    @GetMapping("/my/{userId}")
    public ResponseEntity<?> getMyBookings(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.getMyBookings(userId));
    }

    // Owner gets café bookings
    @GetMapping("/cafe/{cafeId}")
    public ResponseEntity<?> getCafeBookings(@PathVariable Long cafeId) {
        return ResponseEntity.ok(bookingService.getCafeBookings(cafeId));
    }

    // Owner confirms or declines booking
    @PutMapping("/status/{bookingId}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long bookingId,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(bookingId, body.get("status")));
    }

    // Customer cancels booking
    @PutMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }
}