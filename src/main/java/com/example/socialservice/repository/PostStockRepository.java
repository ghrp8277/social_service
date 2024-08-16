package com.example.socialservice.repository;

import com.example.socialservice.entity.PostStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostStockRepository extends JpaRepository<PostStock, Long> {
    Optional<PostStock> findByStockCodeAndStockName(String stockCode, String stockName);
    Optional<PostStock> findByStockCode(String stockCode);
}