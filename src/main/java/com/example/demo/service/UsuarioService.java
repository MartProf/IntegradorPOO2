package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.model.enums.Rol;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo usuario en el sistema
     * @param usuario El usuario a registrar
     * @return El usuario registrado
     * @throws IllegalArgumentException Si los datos son inválidos o el usuario ya existe
     */
    public Usuario registrarUsuario(Usuario usuario) {
        // Validar que el nombre de usuario no esté vacío
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }

        // Validar que la contraseña no esté vacía
        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        // Validar longitud mínima del username
        if (usuario.getUsername().trim().length() < 3) {
            throw new IllegalArgumentException("El nombre de usuario debe tener al menos 3 caracteres");
        }

        // Validar longitud máxima del username
        if (usuario.getUsername().trim().length() > 50) {
            throw new IllegalArgumentException("El nombre de usuario no puede tener más de 50 caracteres");
        }

        // Validar caracteres permitidos en el username
        if (!usuario.getUsername().matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("El nombre de usuario solo puede contener letras, números y guión bajo");
        }

        // Validar longitud mínima de la contraseña
        if (usuario.getPassword().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }

        // Validar longitud máxima de la contraseña
        if (usuario.getPassword().length() > 100) {
            throw new IllegalArgumentException("La contraseña no puede tener más de 100 caracteres");
        }

        // Verificar si el usuario ya existe
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        // Encriptar la contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Asignar rol por defecto (ADMINISTRADOR)
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.ADMINISTRADOR);
        }

        // Guardar y retornar el usuario
        return usuarioRepository.save(usuario);
    }

    /**
     * Busca un usuario por su nombre de usuario
     * @param username El nombre de usuario a buscar
     * @return Un Optional con el usuario si existe
     */
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    /**
     * Verifica si un nombre de usuario ya está en uso
     * @param username El nombre de usuario a verificar
     * @return true si el usuario existe, false en caso contrario
     */
    public boolean existeUsername(String username) {
        return usuarioRepository.findByUsername(username).isPresent();
    }
}
