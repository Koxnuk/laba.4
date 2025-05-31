package com.example.currency.repository;

import com.example.currency.models.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for CurrencyRate entities.
 */
@Repository
public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    /**
     * Find currency rates by abbreviation and date.
     *
     * @param abbreviation the currency abbreviation
     * @param date         the date of the rates
     * @return list of currency rates
     */
    @Query("SELECT cr FROM CurrencyRate cr WHERE cr.currency.abbreviation = :abbreviation AND cr.date = :date")
    List<CurrencyRate> findByCurrencyAbbreviationAndDate(@Param("abbreviation") String abbreviation, @Param("date") LocalDate date);
}