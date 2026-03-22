package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cafe_tables")
@Data
public class CafeTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

    @Column(nullable = false)
    private String tableNumber;

    private Integer capacity;

    @Column(nullable = false)
    private String status = "AVAILABLE";
    // AVAILABLE, OCCUPIED, RESERVED
}
