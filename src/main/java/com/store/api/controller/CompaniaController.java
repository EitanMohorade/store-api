package com.store.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.api.dto.compania.CompaniaCreateDTO;
import com.store.api.dto.compania.CompaniaResponseDTO;
import com.store.api.dto.compania.CompaniaUpdateDTO;
import com.store.api.service.CompaniaService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/companias")
public class CompaniaController {
    
    private final CompaniaService companiaService;

    public CompaniaController(CompaniaService companiaService) {
        this.companiaService = companiaService;
    }

    @GetMapping()
    public ResponseEntity<List<CompaniaResponseDTO>> list() {
        return ResponseEntity.ok(companiaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompaniaResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(companiaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CompaniaResponseDTO> create(@RequestBody CompaniaCreateDTO dto) {

        CompaniaResponseDTO creada = companiaService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompaniaResponseDTO> update(@PathVariable Long id, @RequestBody CompaniaUpdateDTO dto) {

        return ResponseEntity.ok(companiaService.update(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        companiaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
