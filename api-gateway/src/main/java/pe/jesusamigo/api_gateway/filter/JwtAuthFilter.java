package pe.jesusamigo.api_gateway.filter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import pe.jesusamigo.api_gateway.security.JwtTokenProvider;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtTokenProvider tokenProvider;

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().toString();
        log.debug("Procesando petición a: {}", path);

        // Permitir rutas públicas sin autenticación
        if (path.startsWith("/api/auth/")) {
            log.debug("Ruta pública - acceso permitido sin autenticación");
            return chain.filter(exchange);
        }

        HttpHeaders headers = exchange.getRequest().getHeaders();
        log.debug("Cabeceras recibidas: {}", headers);

        if (!headers.containsKey(AUTH_HEADER)) {
            log.warn("Intento de acceso sin cabecera Authorization");
            return unauthorized(exchange, "Falta header Authorization");
        }

        String authHeader = headers.getFirst(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            log.warn("Cabecera Authorization con formato inválido: {}", authHeader);
            return unauthorized(exchange, "Formato inválido del Authorization header");
        }

        String token = authHeader.substring(TOKEN_PREFIX.length());
        log.debug("Token JWT recibido: {}", token);

        try {
            log.debug("Validando token...");
            if (!tokenProvider.validateToken(token)) {
                log.warn("Token no válido: {}", token);
                return unauthorized(exchange, "Token inválido");
            }

            Claims claims = tokenProvider.parseClaims(token);
            log.debug("Claims del token: {}", claims);

            String username = claims.getSubject();
            List<String> roles = claims.get("roles", List.class);

            if (username == null || username.isEmpty()) {
                log.warn("Token no contiene username (subject)");
                return unauthorized(exchange, "Token no contiene información de usuario");
            }

            if (roles == null) {
                log.warn("Token no contiene roles");
                return unauthorized(exchange, "Token no contiene información de roles");
            }

            log.debug("Usuario autenticado: {} con roles: {}", username, roles);

            // Propagar información de usuario
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(r -> r
                            .headers(h -> {
                                h.set("X-User-Name", username);
                                h.set("X-User-Roles", String.join(",", roles));
                                log.debug("Headers modificados - X-User-Name: {}, X-User-Roles: {}",
                                        username, String.join(",", roles));
                            })
                    ).build();

            return chain.filter(modifiedExchange);

        } catch (Exception ex) {
            log.error("Error al validar token JWT - Ruta: {} - Error: {} - Token: {}",
                    path, ex.getMessage(), token, ex);
            return unauthorized(exchange, "Error al validar el token");
        }
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange, String reason) {
        log.warn("Acceso no autorizado: {} - Ruta: {}", reason, exchange.getRequest().getPath());

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

        String errorBody = "{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"" + reason + "\"}";
        log.debug("Respuesta de error: {}", errorBody);

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(errorBody.getBytes());
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}