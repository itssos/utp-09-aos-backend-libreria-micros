package pe.jesusamigo.micro_users.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.jesusamigo.micro_users.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String newUsername);
}
