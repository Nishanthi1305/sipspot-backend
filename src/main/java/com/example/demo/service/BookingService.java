package com.example.demo.service;

import com.example.demo.dto.BookingRequestDto;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CafeRepository cafeRepository;
    @Autowired private CafeTableRepository tableRepository;

    // ── Create Booking ───────────────────────────────────────────────
    public Map<String, Object> createBooking(BookingRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found!"));

        Cafe cafe = cafeRepository.findById(dto.getCafeId())
            .orElseThrow(() -> new RuntimeException("Café not found!"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCafe(cafe);
        booking.setBookingDate(dto.getBookingDate());
        booking.setBookingTime(dto.getBookingTime());
        booking.setGuests(dto.getGuests());
        booking.setStatus("PENDING");

        // Assign table if provided
        if (dto.getTableId() != null) {
            CafeTable table = tableRepository.findById(dto.getTableId())
                .orElseThrow(() -> new RuntimeException("Table not found!"));
            booking.setCafeTable(table);
            table.setStatus("RESERVED");
            tableRepository.save(table);
        }

        bookingRepository.save(booking);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Booking created successfully!");
        response.put("bookingId", booking.getId());
        response.put("status", booking.getStatus());
        return response;
    }

    // ── Get My Bookings ──────────────────────────────────────────────
    public List<Booking> getMyBookings(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    // ── Get Café Bookings (Owner) ────────────────────────────────────
    public List<Booking> getCafeBookings(Long cafeId) {
        return bookingRepository.findByCafeId(cafeId);
    }

    // ── Confirm Booking (Owner) ──────────────────────────────────────
    public Map<String, Object> updateBookingStatus(Long bookingId, String status) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found!"));

        booking.setStatus(status);
        bookingRepository.save(booking);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Booking status updated to " + status);
        response.put("bookingId", bookingId);
        response.put("status", status);
        return response;
    }

    // ── Cancel Booking (Customer) ────────────────────────────────────
    public Map<String, Object> cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found!"));

        booking.setStatus("CANCELLED");

        // Free the table
        if (booking.getCafeTable() != null) {
            CafeTable table = booking.getCafeTable();
            table.setStatus("AVAILABLE");
            tableRepository.save(table);
        }

        bookingRepository.save(booking);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Booking cancelled successfully!");
        return response;
    }
}
