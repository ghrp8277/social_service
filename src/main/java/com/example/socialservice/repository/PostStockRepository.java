package com.example.socialservice.repository;

import com.example.socialservice.entity.PostStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostStockRepository extends JpaRepository<PostStock, Long> {
    Optional<PostStock> findByStockCodeAndStockName(Long stockCode, String stockName);
    Optional<PostStock> findByStockCode(Long stockCode);
}