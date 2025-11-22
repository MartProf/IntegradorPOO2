package com.example.demo.modelo;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.demo.modelo.enums.Rol;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Usuario implements UserDetails { // Implementar UserDetails es la clave
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // El nombre de usuario para loguearse

    @Column(nullable = false)
    private String password; // La contraseña ENCRIPTADA

    @Enumerated(EnumType.STRING)
    private Rol rol; // ADMIN, OPERADOR

    // --- Métodos de UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convertimos nuestro Rol en una "Autoridad" que Spring entienda
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}