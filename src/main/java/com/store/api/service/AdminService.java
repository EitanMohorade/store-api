package com.store.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.store.api.dto.admin.AdminCreateDTO;
import com.store.api.dto.admin.AdminResponseDTO;
import com.store.api.dto.admin.AdminUpdateDTO;
import com.store.api.entity.Admin;
import com.store.api.exception.ResourceNotFoundException;
import com.store.api.exception.ValidationException;
import com.store.api.repository.AdminRepository;

/**
 * Servicio de negocio para la entidad Administrador.
 * 
 * Proporciona operaciones de lógica de negocio para la gestión de administradores,
 * incluyendo validación, creación, actualización y eliminación.
 * 
 * la creacion 
 */
@Service
public class AdminService {
        
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor de AdminService.
     * 
     * @param adminRepository Repositorio de administradores
     * @param passwordEncoder Codificador de contraseñas usando la configuración de seguridad en @Configuration SecurityConfig
    */
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    /**
     * Crea un nuevo administrador y encripta su contraseña.
     * 
     * @param dto AdminCreateDTO con los datos del administrador a crear  
     * @return AdminResponseDTO el cual no contiene la contraseña.
     */ 
    public AdminResponseDTO create(AdminCreateDTO dto) {

        validate(dto);

        Admin admin = new Admin();
        admin.setNombre(dto.getNombre());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));

        Admin saved = adminRepository.save(admin);

        return toResponseDTO(saved);
    }

    /**
     * Actualiza un administrador existente.
     * 
     * @param id ID del administrador a actualizar
     * @param dto AdminUpdateDTO con los datos a actualizar
     * @return AdminResponseDTO el cual no contiene la contraseña.
     * @throws ResourceNotFoundException si el administrador no existe
     */
    public AdminResponseDTO update(Long id, AdminUpdateDTO dto) {

        Admin admin = adminRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        validate(id, dto);

        admin.setNombre(dto.getNombre());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            admin.setPassword(
                passwordEncoder.encode(dto.getPassword())
            );
        }

        return toResponseDTO(adminRepository.save(admin));
    }

    /**
     * Convierte una entidad Admin a AdminResponseDTO para no exponer la contraseña.
     * 
     * @param admin Entidad Admin
     * @return AdminResponseDTO
     */
    private AdminResponseDTO toResponseDTO(Admin admin) {
        return new AdminResponseDTO(
            admin.getId(),
            admin.getNombre()
        );
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
     * Valida los datos de actualización de un administrador.
     * 
     * @param id
     * @param admin AdminUpdateDTO con los datos a validar
     * @throws ValidationException si nombre ya existe o es inválido
     */
    private void validate(Long id, AdminUpdateDTO admin) {
        if (admin.getNombre() == null || admin.getNombre().isBlank()) {
            throw new ValidationException("El nombre del administrador no puede estar vacío");
        }
        if(adminRepository.existsByNombreAndIdNot(admin.getNombre(), id)) {
            throw new ValidationException("Ya existe un administrador con el mismo nombre");
        }
    }

    /**
     * Valida los datos de creación de un administrador.
     * 
     * @param admin AdminCreateDTO con los datos a validar
     * @throws ValidationException si nombre ya existe o es inválido
     */
    private void validate(AdminCreateDTO admin) {
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
