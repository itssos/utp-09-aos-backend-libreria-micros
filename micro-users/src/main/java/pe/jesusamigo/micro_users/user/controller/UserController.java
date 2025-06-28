package pe.jesusamigo.micro_users.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pe.jesusamigo.dto.UserResponseDTO;
import pe.jesusamigo.micro_users.persons.person.dto.PersonCreateDTO;
import pe.jesusamigo.micro_users.persons.person.dto.PersonResponseDTO;
import pe.jesusamigo.micro_users.persons.person.service.PersonService;
import pe.jesusamigo.micro_users.role.entity.Permission;
import pe.jesusamigo.micro_users.user.dto.UserShortResponseDTO;
import pe.jesusamigo.micro_users.user.entity.User;
import pe.jesusamigo.micro_users.user.repository.UserRepository;
import pe.jesusamigo.micro_users.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "Gestión de usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PersonService personService;
    private final UserRepository userRepository;

    @Operation(
            summary = "Listar usuarios (solo datos básicos)",
            description = "Obtiene una lista de usuarios mostrando solo los datos clave (id, username, fullName)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserShortResponseDTO.class)))
    })
    @GetMapping("/short")
    public ResponseEntity<List<UserShortResponseDTO>> getAllUsersShort() {
        List<UserShortResponseDTO> users = userService.getAllUsersShort();
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Crear una nueva persona con usuario",
            description = "Crea una persona y su cuenta de usuario asociada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Persona creada exitosamente", content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content)
    })
    @PostMapping
    public ResponseEntity<PersonResponseDTO> create(
            @Parameter(description = "Datos de persona y usuario", required = true)
            @Valid @RequestBody PersonCreateDTO dto
    ) {
        PersonResponseDTO response = personService.createPerson(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Obtener persona por ID",
            description = "Recupera los datos de una persona por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persona encontrada", content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> getById(
            @Parameter(description = "Id de la persona", required = true)
            @PathVariable Long id
    ) {
        PersonResponseDTO response = personService.getById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Actualizar persona y usuario",
            description = "Actualiza los datos de una persona y su usuario asociado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Persona actualizada correctamente", content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de validación", content = @Content),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> update(
            @Parameter(description = "ID de la persona", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos de la persona y usuario a actualizar", required = true)
            @Valid @RequestBody PersonCreateDTO dto
    ) {
        PersonResponseDTO response = personService.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Eliminar una persona",
            description = "Elimina una persona y su cuenta de usuario asociada."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Persona liminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Persona no encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Id de la persona", required = true)
            @PathVariable Long id
    ) {
        personService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/auth-details")
    public ResponseEntity<UserResponseDTO> getUserAuthDetails(@RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        UserResponseDTO dto = new UserResponseDTO();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole().getName());
        dto.setPermissions(user.getRole().getPermissions().stream()
                .map(Permission::getName)
                .collect(Collectors.toSet()));
        dto.setActive(user.isActive());
        return ResponseEntity.ok(dto);
    }

}
