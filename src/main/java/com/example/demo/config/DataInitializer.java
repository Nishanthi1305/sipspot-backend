package com.example.demo.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {

            if (roleRepository.count() == 0) {

                System.out.println("🔥 Inserting roles...");

                Role admin = new Role();
                admin.setName(Role.RoleName.ROLE_ADMIN);

                Role owner = new Role();
                owner.setName(Role.RoleName.ROLE_CAFE_OWNER);

                Role chef = new Role();
                chef.setName(Role.RoleName.ROLE_CHEF);

                Role waiter = new Role();
                waiter.setName(Role.RoleName.ROLE_WAITER);

                Role customer = new Role();
                customer.setName(Role.RoleName.ROLE_CUSTOMER);

                roleRepository.save(admin);
                roleRepository.save(owner);
                roleRepository.save(chef);
                roleRepository.save(waiter);
                roleRepository.save(customer);

                System.out.println("✅ Roles inserted!");
            }
        };
    }
}