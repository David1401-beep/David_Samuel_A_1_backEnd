package SystemITR.JosueGuinea1A.modules.vehiculos.controller;

import SystemITR.JosueGuinea1A.modules.vehiculos.model.dto.VehiculoResponseDTO;
import SystemITR.JosueGuinea1A.modules.vehiculos.service.VehiculoService;
import SystemITR.JosueGuinea1A.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/vehiculos")
@CrossOrigin
public class VehiculoController {
    private final VehiculoService service;

    public VehiculoController(VehiculoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<VehiculoResponseDTO>>> listar() {
        try {
            List<VehiculoResponseDTO> lista = service.listarTodos();
            return ResponseEntity.ok(new ApiResponse<>(true, "Vehículos cargados", lista));
        } catch (Exception e) {
            log.error("Error al listar los vehículos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error, no pudimos cargar los vehículos. Intenta de nuevo"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VehiculoResponseDTO>> buscarPorId(@PathVariable Long id) {
        try {
            VehiculoResponseDTO dto = service.buscarPorId(id);
            if (dto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "No encontramos ningún vehículo con el id " + id));
            }
            return ResponseEntity.ok(new ApiResponse<>(true, "Vehículo encontrado", dto));
        } catch (Exception e) {
            log.error("Error al buscar el vehículo " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error, no pudimos traer los datos del vehículo"));
        }
    }
}
