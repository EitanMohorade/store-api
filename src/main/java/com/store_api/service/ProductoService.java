package com.store_api.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    public List<String> list() {
        return List.of("Producto A", "Producto B");
    }
}
