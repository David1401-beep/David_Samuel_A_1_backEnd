package SystemITR.JosueGuinea1A.modules.clientes.controller;

import SystemITR.JosueGuinea1A.modules.clientes.model.dto.ClienteRequestDTO;
import SystemITR.JosueGuinea1A.modules.clientes.model.dto.ClienteResponseDTO;
import SystemITR.JosueGuinea1A.modules.clientes.service.ClienteService;
import SystemITR.JosueGuinea1A.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/clientes")
@CrossOrigin
public class ClienteController {
    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteResponseDTO>>> listar() {
        try {
            List<ClienteResponseDTO> lista = service.listarTodos();
            return ResponseEntity.ok(new ApiResponse<>(true, "Clientes cargados", lista));
        } catch (Exception e) {
            log.error("Falló al listar los clientes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "NO pudimos cargar los clientes. Intenta de nuevo"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> buscarPorId(@PathVariable Long id) {
        try {
            ClienteResponseDTO dto = service.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No encontramos ningún cliente con el id " + id));
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Cliente encontrado", dto));
        } catch (Exception e) {
            log.error("Fallo al buscar el cliente " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "NO pudimos traer los datos del cliente"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> registrar(@Valid @RequestBody ClienteRequestDTO json, BindingResult validacion) {
        if (validacion.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, juntarErrores(validacion)));
        }
        try {
            ClienteResponseDTO creado = service.insertar(json);
            log.info("Se registro un cliente nuevo: " + creado);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Cliente registrado con éxito", creado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            log.error("Falló al registrar el cliente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al registrar el cliente"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO json, BindingResult validacion) {
        if (validacion.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, juntarErrores(validacion)));
        }
        try {
            ClienteResponseDTO actualizado = service.actualizar(id, json);
            if (actualizado == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No encontramos ningún cliente con el id " + id));
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Los datos del cliente se actualizaron", actualizado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            log.error("Error al actualizar el cliente " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al actualizar el cliente"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        try {
            if (!service.eliminar(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No encontramos ningún cliente con el id " + id));
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Cliente eliminado"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            log.error("Error al eliminar el cliente " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al eliminar el cliente"));
        }
    }

    private String juntarErrores(BindingResult validacion) {
        return validacion.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(". "));
    }
}
