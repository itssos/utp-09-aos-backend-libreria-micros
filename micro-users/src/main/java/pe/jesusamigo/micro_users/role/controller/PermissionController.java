package pe.jesusamigo.micro_users.role.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.jesusamigo.micro_users.role.entity.Permission;
import pe.jesusamigo.micro_users.role.service.PermissionService;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Tag(name = "Permisos", description = "Operaciones relacionadas con la gestión de permisos del sistema")
public class PermissionController {

    private final PermissionService permissionService;

    @Operation(summary = "Obtener todos los permisos", description = "Devuelve una lista completa de todos los permisos registrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de permisos obtenida correctamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Permission.class)))
    })
    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @Operation(summary = "Obtener permiso por ID", description = "Busca y retorna un permiso específico mediante su identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Permission.class))),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Permission> getPermissionById(
            @Parameter(description = "ID del permiso a buscar", required = true, example = "1")
            @PathVariable Integer id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @Operation(summary = "Obtener permiso por nombre", description = "Busca y retorna un permiso mediante su nombre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Permission.class))),
            @ApiResponse(responseCode = "404", description = "Permiso no encontrado", content = @Content)
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Permission> getPermissionByName(
            @Parameter(description = "Nombre del permiso a buscar", required = true, example = "EDITAR_USUARIOS")
            @PathVariable String name) {
        return ResponseEntity.ok(permissionService.getPermissionByName(name));
    }
}