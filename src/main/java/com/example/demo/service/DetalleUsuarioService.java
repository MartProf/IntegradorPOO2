package com.example.demo.service;

import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación de usuarios para Spring Security.
 * 
 * Esta clase implementa UserDetailsService, que es la interfaz que Spring Security
 * usa para cargar los datos del usuario durante el proceso de autenticación.
 */
@Service // Marca esta clase como un servicio de Spring para inyección de dependencias
public class DetalleUsuarioService implements UserDetailsService {

    @Autowired // Inyección automática del repositorio de usuarios
    private UsuarioRepository usuarioRepository;

    /**
     * Método principal de Spring Security para cargar un usuario por su nombre de usuario.
     * 
     * Este método se llama automáticamente cuando:
     * - Un usuario intenta hacer login
     * - Spring Security necesita verificar las credenciales
     * 
     * @param username El nombre de usuario ingresado en el formulario de login
     * @return UserDetails con los datos del usuario (username, password, roles, etc.)
     * @throws UsernameNotFoundException Si el usuario no existe en la base de datos
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca el usuario en la base de datos por su username
        return usuarioRepository.findByUsername(username)
                // Si no lo encuentra, lanza una excepción indicando que el usuario no existe
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        
        // NOTA: El objeto Usuario debe implementar UserDetails (con métodos como getUsername(), getPassword(), getAuthorities())
        // para que Spring Security pueda usarlo en el proceso de autenticación
    }
}
