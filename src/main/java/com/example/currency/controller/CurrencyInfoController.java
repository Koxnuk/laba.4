package com.example.currency.controller;

import com.example.currency.models.CurrencyInfo;
import com.example.currency.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/currency/info")
@Tag(name = "Currency Info", description = "API for managing currency information")
public class CurrencyInfoController {
    private final CurrencyService currencyService;

    public CurrencyInfoController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Operation(summary = "Get all currencies", description = "Retrieve a list of all currencies")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved currencies"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<CurrencyInfo>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @Operation(summary = "Get all currencies from database", description = "Retrieve all currencies stored in the database")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved currencies"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/db")
    public ResponseEntity<List<CurrencyInfo>> getAllCurrenciesFromDb() {
        return ResponseEntity.ok(currencyService.getAllCurrenciesFromDb());
    }

    @Operation(summary = "Get currency by ID", description = "Retrieve a currency by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved currency"),
            @ApiResponse(responseCode = "404", description = "Currency not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CurrencyInfo> getCurrencyById(@PathVariable Integer id) {
        Optional<CurrencyInfo> currency = currencyService.getCurrencyById(id);
        return currency.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a currency", description = "Create a new currency")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Currency created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid currency data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CurrencyInfo> createCurrency(@Valid @RequestBody CurrencyInfo currencyInfo) {
        CurrencyInfo created = currencyService.createCurrency(currencyInfo);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update a currency", description = "Update an existing currency by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Currency updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid currency data"),
            @ApiResponse(responseCode = "404", description = "Currency not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CurrencyInfo> updateCurrency(@PathVariable Integer id, @Valid @RequestBody CurrencyInfo currencyInfo) {
        CurrencyInfo updated = currencyService.updateCurrency(id, currencyInfo);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a currency", description = "Delete a currency by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Currency deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Currency not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Integer id) {
        currencyService.deleteCurrency(id);
        return ResponseEntity.noContent().build();
    }
}