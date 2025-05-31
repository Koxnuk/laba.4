package com.example.currency.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "currency_rate")
@Data
public class CurrencyRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("Cur_OfficialRate")
    @Column(name = "cur_official_rate")
    private BigDecimal officialRate;

    @JsonProperty("Cur_Scale")
    @Column(name = "cur_scale")
    private Integer scale;

    @JsonProperty("Date")
    @Column(name = "date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private CurrencyInfo currency;
}