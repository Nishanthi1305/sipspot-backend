package com.example.demo.repository;

import com.example.demo.entity.CafeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CafeTableRepository extends JpaRepository<CafeTable, Long> {
    List<CafeTable> findByCafeId(Long cafeId);
    List<CafeTable> findByCafeIdAndStatus(Long cafeId, String status);
}
