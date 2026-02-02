package com.store.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.store.api.dto.admin.AdminCreateDTO;
import com.store.api.dto.admin.AdminResponseDTO;
import com.store.api.dto.admin.AdminUpdateDTO;
import com.store.api.service.AdminService;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/api/admins")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<AdminResponseDTO> create(@RequestBody AdminCreateDTO dto) {

        AdminResponseDTO creada = adminService.create(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(creada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<AdminResponseDTO> update(@PathVariable Long id, @RequestBody AdminUpdateDTO dto) {

        return ResponseEntity.ok(adminService.update(id, dto));
    }
    
}
