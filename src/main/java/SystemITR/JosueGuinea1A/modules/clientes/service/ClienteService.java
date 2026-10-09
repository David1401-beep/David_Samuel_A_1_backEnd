package SystemITR.JosueGuinea1A.modules.clientes.service;

import SystemITR.JosueGuinea1A.modules.clientes.model.dto.ClienteRequestDTO;
import SystemITR.JosueGuinea1A.modules.clientes.model.dto.ClienteResponseDTO;
import SystemITR.JosueGuinea1A.modules.clientes.model.entity.Cliente;
import SystemITR.JosueGuinea1A.modules.clientes.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    @Autowired
    private ClienteRepository repo;

    public List<ClienteResponseDTO> listarTodos() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        return repo.findById(id).map(this::convertirADTO).orElse(null);
    }

    public ClienteResponseDTO insertar(ClienteRequestDTO dto) {
        if (repo.existsByEmail(dto.getEmail().trim())) {
            throw new IllegalArgumentException("Ya hay un cliente registrado con el correo " + dto.getEmail());
        }
        Cliente cliente = new Cliente();
        pasarDatos(dto, cliente);
        return convertirADTO(repo.save(cliente));
    }

    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        Optional<Cliente> registro = repo.findById(id);
        if (registro.isEmpty()) {
            return null;
        }

        if (repo.existsByEmailAndIdNot(dto.getEmail().trim(), id)) {
            throw new IllegalArgumentException("Ese correo ya lo usa otro cliente: " + dto.getEmail());
        }
        Cliente cliente = registro.get();
        pasarDatos(dto, cliente);
        return convertirADTO(repo.save(cliente));
    }

    public boolean eliminar(Long id) {
        if (!repo.existsById(id)) {
            return false;
        }
        try {
            repo.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("No se puede eliminar a este cliente porque ya tiene reservas a su nombre");
        }
    }

    private void pasarDatos(ClienteRequestDTO dto, Cliente cliente) {
        cliente.setNombre(dto.getNombre().trim());
        cliente.setApellido(dto.getApellido().trim());
        cliente.setTelefono(dto.getTelefono().trim());
        cliente.setEmail(dto.getEmail().trim());
        cliente.setDireccion(dto.getDireccion());
    }

    private ClienteResponseDTO convertirADTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setApellido(cliente.getApellido());
        dto.setTelefono(cliente.getTelefono());
        dto.setEmail(cliente.getEmail());
        dto.setDireccion(cliente.getDireccion());
        return dto;
    }
}
