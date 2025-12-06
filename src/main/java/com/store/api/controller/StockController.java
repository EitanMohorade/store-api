package com.store.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    @GetMapping
    public Map<String, Integer> status() {
        return Map.of("Producto A", 10, "Producto B", 5);
    }
}
