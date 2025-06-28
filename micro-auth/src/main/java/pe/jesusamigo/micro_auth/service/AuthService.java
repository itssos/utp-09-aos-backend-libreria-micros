package pe.jesusamigo.micro_auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pe.jesusamigo.dto.PersonResponseDTO;
import pe.jesusamigo.dto.UserResponseDTO;
import pe.jesusamigo.micro_auth.client.UserClient;
import pe.jesusamigo.micro_auth.dto.JwtAuthResponse;
import pe.jesusamigo.micro_auth.dto.LoginRequest;
import pe.jesusamigo.micro_auth.security.CustomUserDetailsService;
import pe.jesusamigo.micro_auth.security.JwtTokenProvider;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String TOKEN_TYPE = "Bearer";

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final UserClient userClient;

    public JwtAuthResponse authenticate(LoginRequest request) {
        // 1. Autenticar usando el AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // 2. Obtener UserDetails usando el servicio personalizado
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // 3. Obtener datos adicionales del usuario
        PersonResponseDTO person = userClient.getPersonByUsername(request.getUsername());

        // 4. Generar el token
        String jwt = tokenProvider.generateToken(userDetails);

        return new JwtAuthResponse(jwt, TOKEN_TYPE, person);
    }
}