package com.example.currency.repository;

import com.example.currency.models.CurrencyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CurrencyInfoRepository extends JpaRepository<CurrencyInfo, Integer> {
}
