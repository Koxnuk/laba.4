package com.example.currency.controller;

import com.example.currency.models.CurrencyRate;
import com.example.currency.service.CurrencyConversionService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/currency/rates")
@Tag(name = "Currency Rates", description = "API for managing currency rates and conversions")
public class CurrencyRateController {
    private final CurrencyConversionService conversionService;

    public CurrencyRateController(CurrencyConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Operation(summary = "Convert currency", description = "Convert an amount from one currency to another")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conversion successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/convert")
    public ResponseEntity<?> convert(
            @RequestParam Integer from,
            @RequestParam Integer to,
            @RequestParam BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Amount must be greater than zero"));
        }
        BigDecimal result = conversionService.convertCurrency(from, to, amount);
        return ResponseEntity.ok(Map.of(
                "amount", amount,
                "from", from,
                "to", to,
                "result", result));
    }

    @Operation(summary = "Get all rates", description = "Retrieve a list of all currency rates")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved rates"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<CurrencyRate>> getAllRates() {
        return ResponseEntity.ok(conversionService.getAllRates());
    }

    @Operation(summary = "Get rate by ID", description = "Retrieve a currency rate by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved rate"),
            @ApiResponse(responseCode = "404", description = "Rate not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CurrencyRate> getRateById(@PathVariable Long id) {
        Optional<CurrencyRate> rate = conversionService.getRateById(id);
        return rate.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a rate", description = "Create a new currency rate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rate created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid rate data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<CurrencyRate> createRate(@Valid @RequestBody CurrencyRate rate) {
        CurrencyRate created = conversionService.createRate(rate);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Update a rate", description = "Update an existing currency rate by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Rate updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid rate data"),
            @ApiResponse(responseCode = "404", description = "Rate not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CurrencyRate> updateRate(@PathVariable Long id, @Valid @RequestBody CurrencyRate rate) {
        CurrencyRate updated = conversionService.updateRate(id, rate);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete a rate", description = "Delete a currency rate by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Rate deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Rate not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRate(@PathVariable Long id) {
        conversionService.deleteRate(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get rates by abbreviation and date", description = "Retrieve currency rates by abbreviation and date")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved rates"),
            @ApiResponse(responseCode = "400", description = "Invalid abbreviation or date"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/by-abbreviation")
    public ResponseEntity<List<CurrencyRate>> getRatesByAbbreviationAndDate(
            @RequestParam String abbreviation,
            @RequestParam LocalDate date) {
        List<CurrencyRate> rates = conversionService.getRatesByAbbreviationAndDate(abbreviation, date);
        return ResponseEntity.ok(rates);
    }
}