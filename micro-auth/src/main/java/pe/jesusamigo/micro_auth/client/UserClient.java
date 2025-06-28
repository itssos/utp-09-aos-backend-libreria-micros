package pe.jesusamigo.micro_auth.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pe.jesusamigo.dto.PersonResponseDTO;
import pe.jesusamigo.dto.UserResponseDTO;

@Service
@RequiredArgsConstructor
public class UserClient {

    private final RestTemplate restTemplate;

    @CircuitBreaker(name = "users", fallbackMethod = "fallbackPerson")
    public PersonResponseDTO getPersonByUsername(String username) {
        String url = "http://micro-users/api/persons/by-username/" + username;
        return restTemplate.getForObject(url, PersonResponseDTO.class);
    }

    @CircuitBreaker(name = "users", fallbackMethod = "fallbackUser")
    public UserResponseDTO getUserByUsername(String username) {
        String url = "http://micro-users/api/users/by-username/" + username;
        return restTemplate.getForObject(url, UserResponseDTO.class);
    }

    @CircuitBreaker(name = "users", fallbackMethod = "fallbackUser")
    public UserResponseDTO getUserAuthDetails(String username) {
        String url = "http://micro-users/api/users/auth-details?username=" + username;
        return restTemplate.getForObject(url, UserResponseDTO.class);
    }

    private UserResponseDTO fallbackUser(String username, Throwable e) {
        throw new RuntimeException("User Service no disponible");
    }

    private PersonResponseDTO fallbackPerson(String username, Throwable e) {
        return null;
    }

    public UserResponseDTO fallback(String username, Throwable ex) {
        throw new RuntimeException("User service unavailable", ex);
    }
}