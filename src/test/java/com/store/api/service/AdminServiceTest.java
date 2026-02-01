package com.store.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.store.api.dto.admin.AdminCreateDTO;
import com.store.api.dto.admin.AdminResponseDTO;
import com.store.api.dto.admin.AdminUpdateDTO;
import com.store.api.entity.Admin;
import com.store.api.repository.AdminRepository;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    
    @InjectMocks
    private AdminService adminService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AdminRepository adminRepository;

    // Test de funcion create
    @Test
    void create_deberiaCrearAdminYNoExponerPassword(){
        AdminCreateDTO dto = new AdminCreateDTO();
        dto.setNombre("admin test");
        dto.setPassword("1234");

        when(passwordEncoder.encode("1234"))
            .thenReturn("password_encriptado");

        when(adminRepository.save(any(Admin.class)))
            .thenAnswer(invocation -> {
                Admin saved = invocation.getArgument(0);
                saved.setId(1L);
                return saved;
            });

        AdminResponseDTO response = adminService.create(dto);

        assertNotNull(response);
        assertEquals("admin test", response.getNombre());
        assertEquals(1L, response.getId());
    }

    @Test
    void create_deberiaLanzarExcepcionSiNombreYaExiste(){
        AdminCreateDTO dto = new AdminCreateDTO();
        dto.setNombre("admin test");
        dto.setPassword("1234");

        AdminCreateDTO dtoRepetido = new AdminCreateDTO();
        dtoRepetido.setNombre("admin test");
        dtoRepetido.setPassword("4321");

        when(adminRepository.existsByNombre("admin test"))
            .thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> {
            adminService.create(dtoRepetido);
        });

        assertEquals("Ya existe un administrador con el mismo nombre", exception.getMessage());
    }

    @Test
    void create_deberiaLanzarExcepcionSiNombreEsNulo(){
        AdminCreateDTO dto = new AdminCreateDTO();
        dto.setNombre(null);
        dto.setPassword("1234");

        Exception exception = assertThrows(Exception.class, () -> {
            adminService.create(dto);
        });

        assertEquals("El nombre del administrador no puede estar vacío", exception.getMessage());
    }

    @Test
    void create_deberiaLanzarExcepcionSiNombreEsVacio(){
        AdminCreateDTO dto = new AdminCreateDTO();
        dto.setNombre("");
        dto.setPassword("1234");

        Exception exception = assertThrows(Exception.class, () -> {
            adminService.create(dto);
        });

        assertEquals("El nombre del administrador no puede estar vacío", exception.getMessage());
    }

    @Test
    void create_deberiaLanzarExcepcionSiPasswordEsNulo(){
        AdminCreateDTO dto = new AdminCreateDTO();
        dto.setNombre("admin test");
        dto.setPassword(null);

        Exception exception = assertThrows(Exception.class, () -> {
            adminService.create(dto);
        });

        assertEquals("La contraseña del administrador no puede estar vacía", exception.getMessage());
    } 

    @Test
    void create_deberiaLanzarExcepcionSiPasswordEsVacio(){
        AdminCreateDTO dto = new AdminCreateDTO();
        dto.setNombre("admin test");
        dto.setPassword("   ");

        Exception exception = assertThrows(Exception.class, () -> {
            adminService.create(dto);
        });

        assertEquals("La contraseña del administrador no puede estar vacía", exception.getMessage());
    }
    // Test de funcion update
    @Test
    void update_DebeActualizarAdminCorrectamente(){
        Admin adminExistente = new Admin();
        adminExistente.setId(1L);
        adminExistente.setNombre("admin viejo");
        adminExistente.setPassword("pass vieja");

        AdminUpdateDTO dto = new AdminUpdateDTO();
        dto.setNombre("admin actualizado");
        dto.setPassword("1234");

        when(adminRepository.findById(1L))
                .thenReturn(Optional.of(adminExistente));

        when(passwordEncoder.encode("1234"))
                .thenReturn("pass nueva");

        when(adminRepository.save(any(Admin.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AdminResponseDTO response = adminService.update(1L, dto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("admin actualizado", response.getNombre());
        
    }

    @Test
    void update_deberiaLanzarExcepcionSiAdminYaExisteConMismoNombre() {
        Admin adminExistente = new Admin();
        adminExistente.setId(1L);
        adminExistente.setNombre("admin viejo");

        AdminUpdateDTO dto = new AdminUpdateDTO();
        dto.setNombre("otro admin");
        dto.setPassword("1234");

        when(adminRepository.findById(1L))
                .thenReturn(Optional.of(adminExistente));

        when(adminRepository.existsByNombreAndIdNot("otro admin", 1L))
                .thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class,
                () -> adminService.update(1L, dto));

        assertEquals("Ya existe un administrador con el mismo nombre",
                exception.getMessage());

        verify(adminRepository, never()).save(any());
    }

    @Test
    void update_deberiaLanzarExcepcionSiNombreEsNulo() {
        Admin adminExistente = new Admin();
        adminExistente.setId(1L);
        adminExistente.setNombre("admin viejo");

        AdminUpdateDTO dto = new AdminUpdateDTO();
        dto.setNombre(null);
        dto.setPassword("1234");

        when(adminRepository.findById(1L))
                .thenReturn(Optional.of(adminExistente));

        Exception exception = assertThrows(RuntimeException.class,
                () -> adminService.update(1L, dto));

        assertEquals("El nombre del administrador no puede estar vacío",
                exception.getMessage());

        verify(adminRepository, never()).save(any());
    }

    @Test
    void update_deberiaLanzarExcepcionSiNombreEsVacio() {
        Admin adminExistente = new Admin();
        adminExistente.setId(1L);
        adminExistente.setNombre("admin viejo");

        AdminUpdateDTO dto = new AdminUpdateDTO();
        dto.setNombre("   ");
        dto.setPassword("1234");

        when(adminRepository.findById(1L))
                .thenReturn(Optional.of(adminExistente));

        Exception exception = assertThrows(RuntimeException.class,
                () -> adminService.update(1L, dto));

        assertEquals("El nombre del administrador no puede estar vacío",
                exception.getMessage());

        verify(adminRepository, never()).save(any());
    }

    @Test
    void update_deberiaActualizarAdminSinCambiarPasswordSiPasswordEsNulo(){
        Admin adminExistente = new Admin();
        adminExistente.setId(1L);
        adminExistente.setNombre("admin viejo");
        adminExistente.setPassword("pass vieja");

        AdminUpdateDTO dto = new AdminUpdateDTO();
        dto.setNombre("admin actualizado");
        dto.setPassword(null);

        when(adminRepository.findById(1L))
                .thenReturn(Optional.of(adminExistente));

        when(adminRepository.save(any(Admin.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AdminResponseDTO response = adminService.update(1L, dto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("admin actualizado", response.getNombre());
        
        verify(passwordEncoder, never()).encode(any());
    }
    // Test de funcion delete
    @Test
    void delete_DebeEliminarAdminCorrectamente(){
        Long adminId = 1L;

        when(adminRepository.existsById(adminId))
                .thenReturn(true);
        
        adminService.delete(adminId);

        verify(adminRepository).deleteById(adminId);
    }

    @Test
    void delete_DebeLanzarExcepcionSiAdminNoExiste(){
        Long adminId = 1L;

        when(adminRepository.existsById(adminId))
                .thenReturn(false);
        
        Exception exception = assertThrows(Exception.class, () -> {
            adminService.delete(adminId);
        });

        assertEquals("Recurso no encontrado", exception.getMessage());
    }
}
