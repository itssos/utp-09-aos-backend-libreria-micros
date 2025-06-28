package pe.jesusamigo.micro_users.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.jesusamigo.exception.ResourceNotFoundException;
import pe.jesusamigo.micro_users.persons.person.repository.PersonRepository;
import pe.jesusamigo.micro_users.role.entity.Role;
import pe.jesusamigo.micro_users.role.repository.RoleRepository;
import pe.jesusamigo.micro_users.user.dto.UserCreateDTO;
import pe.jesusamigo.micro_users.user.dto.UserResponseDTO;
import pe.jesusamigo.micro_users.user.dto.UserShortResponseDTO;
import pe.jesusamigo.micro_users.user.entity.User;
import pe.jesusamigo.micro_users.user.mapper.UserMapper;
import pe.jesusamigo.micro_users.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final PersonRepository personRepository;

    public UserResponseDTO createUser(UserCreateDTO dto) {
        validateNewUser(dto);

        User user = userMapper.fromCreateDto(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = getRoleByName(dto.getRole());
        user.setRole(role);

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserResponseDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return userMapper.toDto(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<UserShortResponseDTO> getAllUsersShort() {
        return personRepository.findAll()
                .stream()
                .filter(person -> person.getUser() != null)
                .filter(person -> person.getUser().getId() != 1)
                .map(person -> userMapper.toShortDto(person.getUser(), person))
                .collect(Collectors.toList());
    }




    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    public UserResponseDTO updateUser(Integer id, UserCreateDTO dto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        validateUserUpdate(dto, existingUser);

        existingUser.setUsername(dto.getUsername());
        existingUser.setEmail(dto.getEmail());
        existingUser.setPassword(passwordEncoder.encode(dto.getPassword()));

        Role role = getRoleByName(dto.getRole());
        existingUser.setRole(role);

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    // ========================
    // Validaciones y utilitarios
    // ========================

    private void validateNewUser(UserCreateDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalStateException("Ya existe un usuario con el nombre: " + dto.getUsername());
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank() &&
                userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalStateException("Ya existe un usuario con el correo: " + dto.getEmail());
        }
    }

    private void validateUserUpdate(UserCreateDTO dto, User existingUser) {
        if (!existingUser.getUsername().equals(dto.getUsername())) {
            Optional<User> byUsername = userRepository.findByUsername(dto.getUsername());
            if (byUsername.isPresent() && !byUsername.get().getId().equals(existingUser.getId())) {
                throw new IllegalStateException("El nombre de usuario ya está en uso: " + dto.getUsername());
            }
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()
                && !dto.getEmail().equals(existingUser.getEmail())) {
            Optional<User> byEmail = userRepository.findByEmail(dto.getEmail());
            if (byEmail.isPresent() && !byEmail.get().getId().equals(existingUser.getId())) {
                throw new IllegalStateException("El correo ya está en uso: " + dto.getEmail());
            }
        }
    }

    private Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));
    }
}
