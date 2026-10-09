package SystemITR.JosueGuinea1A.modules.reservas.controller;

import SystemITR.JosueGuinea1A.modules.reservas.model.dto.ReservaRequestDTO;
import SystemITR.JosueGuinea1A.modules.reservas.model.dto.ReservaResponseDTO;
import SystemITR.JosueGuinea1A.modules.reservas.service.ReservaService;
import SystemITR.JosueGuinea1A.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/reservas")
@CrossOrigin
public class ReservaController {
    private final ReservaService service;

    public ReservaController(ReservaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservaResponseDTO>>> listar() {
        try {
            List<ReservaResponseDTO> lista = service.listarTodos();
            return ResponseEntity.ok(new ApiResponse<>(true, "Reservas cargadas", lista));
        } catch (Exception e) {
            log.error("Falló al listar las reservas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al cargar las reservas. Intenta de nuevo"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservaResponseDTO>> buscarPorId(@PathVariable Long id) {
        try {
            ReservaResponseDTO dto = service.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No encontramos ninguna reserva con el id " + id));
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Reserva encontrada", dto));
        } catch (Exception e) {
            log.error("Falló al buscar la reserva " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error traer los datos de la reserva"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReservaResponseDTO>> registrar(@Valid @RequestBody ReservaRequestDTO json, BindingResult validacion) {
        if (validacion.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, juntarErrores(validacion)));
        }
        try {
            ReservaResponseDTO creada = service.insertar(json);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Reserva registrada. El total a pagar es $" + creada.getTotalPago(), creada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            log.error("Error al registrar la reserva: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al registrar la reserva"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservaResponseDTO>> actualizar(@PathVariable Long id, @Valid @RequestBody ReservaRequestDTO json, BindingResult validacion) {
        if (validacion.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, juntarErrores(validacion)));
        }
        try {
            ReservaResponseDTO actualizada = service.actualizar(id, json);
            if (actualizada == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No encontramos ninguna reserva con el id " + id));
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "La reserva se actualizó", actualizada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            log.error("Error al actualizar la reserva " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al actualizar la reserva"));
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<ApiResponse<ReservaResponseDTO>> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> json) {
        try {
            ReservaResponseDTO actualizada = service.cambiarEstado(id, json.get("estado"));
            if (actualizada == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No encontramos ninguna reserva con el id " + id));
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "La reserva ahora está " + actualizada.getEstado(), actualizada));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            log.error("Error cambiar el estado de la reserva " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Ups, algo salió mal al cambiar el estado"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        try {
            if (!service.eliminar(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No encontramos ninguna reserva con el id " + id));
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Reserva eliminada"));
        } catch (Exception e) {
            log.error("Error al eliminar la reserva " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error algo salió mal al eliminar la reserva"));
        }
    }

    private String juntarErrores(BindingResult validacion) {
        return validacion.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(". "));
    }
}
