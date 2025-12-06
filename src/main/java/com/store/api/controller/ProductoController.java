package com.store.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @GetMapping
    public List<String> list() {
        return List.of("Producto A", "Producto B");
    }
}
