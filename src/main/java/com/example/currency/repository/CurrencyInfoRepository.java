package com.example.currency.repository;

import com.example.currency.models.CurrencyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for CurrencyInfo entities.
 */
@Repository
public interface CurrencyInfoRepository extends JpaRepository<CurrencyInfo, Integer> {
}