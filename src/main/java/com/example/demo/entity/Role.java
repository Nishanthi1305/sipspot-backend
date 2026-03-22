package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleName name;

    public enum RoleName {
        ROLE_ADMIN,
        ROLE_CAFE_OWNER,
        ROLE_CHEF,
        ROLE_WAITER,
        ROLE_CUSTOMER
    }
}
