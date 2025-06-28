package pe.jesusamigo.micro_users.role.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.jesusamigo.micro_users.role.entity.Role;
import pe.jesusamigo.micro_users.role.service.RolePermissionService;

@RestController
@RequestMapping("/api/roles/{roleId}/permissions")
@Tag(name = "Permisos de Rol", description = "Asignación y eliminación de permisos a los roles del sistema")
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    public RolePermissionController(RolePermissionService rolePermissionService) {
        this.rolePermissionService = rolePermissionService;
    }

    @Operation(
            summary = "Asignar permiso a un rol",
            description = "Asigna un permiso existente a un rol específico utilizando el nombre del permiso."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso asignado correctamente al rol",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
            @ApiResponse(responseCode = "404", description = "Rol o permiso no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Permiso ya asignado o datos inválidos", content = @Content)
    })
    @PostMapping("/{permissionName}")
    public ResponseEntity<Role> assignPermissionToRole(
            @Parameter(description = "ID del rol al que se le asignará el permiso", required = true, example = "1")
            @PathVariable Integer roleId,

            @Parameter(description = "Nombre del permiso a asignar", required = true, example = "EDITAR_USUARIOS")
            @PathVariable String permissionName) {

        Role updatedRole = rolePermissionService.addPermissionToRole(roleId, permissionName);
        return ResponseEntity.ok(updatedRole);
    }

    @Operation(
            summary = "Eliminar permiso de un rol",
            description = "Elimina un permiso previamente asignado a un rol utilizando el nombre del permiso."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permiso eliminado correctamente del rol",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Role.class))),
            @ApiResponse(responseCode = "404", description = "Rol o permiso no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Permiso no estaba asignado o datos inválidos", content = @Content)
    })
    @DeleteMapping("/{permissionName}")
    public ResponseEntity<Role> removePermissionFromRole(
            @Parameter(description = "ID del rol del que se eliminará el permiso", required = true, example = "1")
            @PathVariable Integer roleId,

            @Parameter(description = "Nombre del permiso a eliminar", required = true, example = "EDITAR_USUARIOS")
            @PathVariable String permissionName) {

        Role updatedRole = rolePermissionService.removePermissionFromRole(roleId, permissionName);
        return ResponseEntity.ok(updatedRole);
    }
}
