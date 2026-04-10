package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.example.demo.config.JwtProperties;

// ✅ ADD THESE
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    // ✅ ADD THIS BLOCK
    @Bean
    CommandLineRunner initRoles(RoleRepository roleRepository) {
        return args -> {

            if (roleRepository.count() == 0) {

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
            }
        };
    }
}