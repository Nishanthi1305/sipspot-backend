package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "cafes")
@Data
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;
    private String cuisine;
    private String image;
    private Double rating = 4.5;
    private Integer reviews = 0;
    private Integer totalTables = 0;
    private boolean open = true;
    private String tag;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
