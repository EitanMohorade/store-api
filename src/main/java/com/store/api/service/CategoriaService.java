package com.store.api.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    public List<String> list() {
        return List.of("Categoria X", "Categoria Y");
    }
}
