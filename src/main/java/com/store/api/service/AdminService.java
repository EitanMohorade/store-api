package com.store.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.store.api.entity.Admin;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.repository.AdminRepository;

@Service
public class AdminService {
        
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public Admin create(Admin admin) {
        validate(admin);

        admin.setPassword(
            passwordEncoder.encode(admin.getPassword())
        );

        return adminRepository.save(admin);
    }


    /**
     * Actualiza un administrador existente.
     * 
     * @param id ID del administrador a actualizar
     * @param admin Administrador con datos actualizados
     * @return Administrador actualizado
     * @throws ValidationException si el administrador no cumple validaciones
     */
    public Admin update(Long id, Admin admin) {
        validate(admin);

        admin.setId(id);
        return adminRepository.save(admin);
    }

    /**
     * Elimina un administrador por su ID.
     * 
     * @param id ID del administrador a eliminar
     * @throws ResourceNotFoundException si el administrador no existe
     */
    public void delete(Long id) {
        if (!adminRepository.existsById(id)) {
            throw new ResourceNotFoundException();
        }
        adminRepository.deleteById(id);
    }

    /**
     * Valida los datos del administrador.
     * 
     * @param admin Administrador a validar
     * @throws ValidationException si el administrador no cumple validaciones
     */
    private void validate(Admin admin) {
        if (admin.getNombre() == null || admin.getNombre().isBlank()) {
            throw new ValidationException("El nombre del administrador no puede estar vacío");
        }
        if (admin.getPassword() == null || admin.getPassword().isBlank()) {
            throw new ValidationException("La contraseña del administrador no puede estar vacía");
        }
        if(adminRepository.existsByNombre(admin.getNombre())) {
            throw new ValidationException("Ya existe un administrador con el mismo nombre");
        }
    }

}
