package SystemITR.JosueGuinea1A.modules.vehiculos.service;

import SystemITR.JosueGuinea1A.modules.vehiculos.model.dto.VehiculoResponseDTO;
import SystemITR.JosueGuinea1A.modules.vehiculos.model.entity.Vehiculo;
import SystemITR.JosueGuinea1A.modules.vehiculos.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehiculoService {
    @Autowired
    private VehiculoRepository repo;

    public List<VehiculoResponseDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public VehiculoResponseDTO buscarPorId(Long id) {
        return repo.findById(id).map(this::convertirADTO).orElse(null);
    }

    private VehiculoResponseDTO convertirADTO(Vehiculo v) {
        VehiculoResponseDTO dto = new VehiculoResponseDTO();
        dto.setId(v.getId());
        dto.setMarca(v.getMarca());
        dto.setModelo(v.getModelo());
        dto.setCapacidad(v.getCapacidad());
        dto.setPrecioDia(v.getPrecioDia());
        dto.setPlaca(v.getPlaca());
        return dto;
    }
}
