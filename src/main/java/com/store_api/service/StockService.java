package com.store_api.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StockService {
    public Map<String, Integer> status() {
        return Map.of("Producto A", 10, "Producto B", 5);
    }
}
