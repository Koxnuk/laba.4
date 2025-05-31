package com.example.currency.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "currency_info")
@Data
public class CurrencyInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonProperty("Cur_Code")
    @Column(name = "cur_code")
    private String code;

    @JsonProperty("Cur_Abbreviation")
    @Column(name = "cur_abbreviation")
    private String abbreviation;

    @JsonProperty("Cur_Name")
    @Column(name = "cur_name")
    private String name;

    @JsonProperty("Cur_Scale")
    @Column(name = "cur_scale")
    private Integer scale;

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurrencyRate> rates = new ArrayList<>();
}