package pe.jesusamigo.micro_auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.jesusamigo.dto.UserResponseDTO;
import pe.jesusamigo.micro_auth.client.UserClient;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserResponseDTO user = userClient.getUserAuthDetails(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        if (!user.isActive()) {
            throw new UsernameNotFoundException("Usuario inactivo");
        }

        return buildUserDetails(user);
    }

    private UserDetails buildUserDetails(UserResponseDTO user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Debe venir cifrado desde micro-users
                .disabled(!user.isActive())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .authorities(getAuthorities(user))
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserResponseDTO user) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Agregar rol como autoridad (prefijo ROLE_ requerido para hasRole())
        if (user.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        }

        // Agregar permisos individuales
        if (user.getPermissions() != null) {
            user.getPermissions().stream()
                    .filter(Objects::nonNull)
                    .map(SimpleGrantedAuthority::new)
                    .forEach(authorities::add);
        }

        return authorities;
    }
}