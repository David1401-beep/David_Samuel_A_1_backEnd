package SystemITR.JosueGuinea1A.modules.reservas.service;

import SystemITR.JosueGuinea1A.modules.clientes.model.entity.Cliente;
import SystemITR.JosueGuinea1A.modules.clientes.repository.ClienteRepository;
import SystemITR.JosueGuinea1A.modules.reservas.model.dto.ReservaRequestDTO;
import SystemITR.JosueGuinea1A.modules.reservas.model.dto.ReservaResponseDTO;
import SystemITR.JosueGuinea1A.modules.reservas.model.entity.Reserva;
import SystemITR.JosueGuinea1A.modules.reservas.repository.ReservaRepository;
import SystemITR.JosueGuinea1A.modules.vehiculos.model.entity.Vehiculo;
import SystemITR.JosueGuinea1A.modules.vehiculos.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaService {
    private static final String ESTADO_INICIAL = "CONFIRMADA";
    private static final List<String> ESTADOS_PERMITIDOS = List.of("PENDIENTE", "CONFIRMADA", "CANCELADA", "FINALIZADA");

    private static final List<String> ESTADOS_ACTIVOS = List.of("PENDIENTE", "CONFIRMADA");

    @Autowired
    private ReservaRepository repo;
    @Autowired
    private ClienteRepository clienteRepo;
    @Autowired
    private VehiculoRepository vehiculoRepo;

    public List<ReservaResponseDTO> listarTodos() {
        return repo.findAllByOrderByFechaReservaDesc().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ReservaResponseDTO buscarPorId(Long id) {
        return repo.findById(id).map(this::convertirADTO).orElse(null);
    }

    public ReservaResponseDTO insertar(ReservaRequestDTO dto) {
        Cliente cliente = buscarCliente(dto.getIdCliente());
        Vehiculo vehiculo = buscarVehiculo(dto.getIdVehiculo());

        validarCapacidad(dto.getCantidadPasajeros(), vehiculo);
        validarVehiculoLibre(vehiculo.getId(), dto.getFechaReserva(), null);

        Reserva reserva = new Reserva();
        pasarDatos(dto, reserva, cliente, vehiculo);
        reserva.setEstado(ESTADO_INICIAL);
        return convertirADTO(repo.save(reserva));
    }

    public ReservaResponseDTO actualizar(Long id, ReservaRequestDTO dto) {
        Optional<Reserva> registro = repo.findById(id);
        if (registro.isEmpty()) {
            return null;
        }
        Reserva reserva = registro.get();
        Cliente cliente = buscarCliente(dto.getIdCliente());
        Vehiculo vehiculo = buscarVehiculo(dto.getIdVehiculo());

        validarCapacidad(dto.getCantidadPasajeros(), vehiculo);
        if (esActivo(reserva.getEstado())) {
            validarVehiculoLibre(vehiculo.getId(), dto.getFechaReserva(), id);
        }
        pasarDatos(dto, reserva, cliente, vehiculo);
        return convertirADTO(repo.save(reserva));
    }

    public ReservaResponseDTO cambiarEstado(Long id, String nuevoEstado) {
        String estado = nuevoEstado == null ? "" : nuevoEstado.trim().toUpperCase();
        if (!ESTADOS_PERMITIDOS.contains(estado)) {
            throw new IllegalArgumentException("Ese estado no existe. Puedes usar: " + String.join(", ", ESTADOS_PERMITIDOS));
        }
        Optional<Reserva> registro = repo.findById(id);
        if (registro.isEmpty()) {
            return null;
        }
        Reserva reserva = registro.get();

        if (esActivo(estado)) {
            validarVehiculoLibre(reserva.getVehiculo().getId(), reserva.getFechaReserva(), id);
        }
        reserva.setEstado(estado);
        return convertirADTO(repo.save(reserva));
    }

    public boolean eliminar(Long id) {
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }

    private void validarCapacidad(Integer pasajeros, Vehiculo vehiculo) {
        if (pasajeros > vehiculo.getCapacidad()) {
            throw new IllegalArgumentException("Ese vehículo no alcanza para " + pasajeros
                    + " pasajeros, solo tiene capacidad para " + vehiculo.getCapacidad() + " personas");
        }
    }

    private void validarVehiculoLibre(Long idVehiculo, LocalDate fecha, Long idReservaActual) {
        List<Reserva> ocupadas = repo.findByVehiculoIdAndFechaReservaAndEstadoIn(idVehiculo, fecha, ESTADOS_ACTIVOS);
        for (Reserva otra : ocupadas) {
            if (!otra.getId().equals(idReservaActual)) {
                throw new IllegalArgumentException("Ese vehículo ya está reservado para el " + fecha
                        + " (" + otra.getNombreReserva() + ")");
            }
        }
    }

    private boolean esActivo(String estado) {
        return estado != null && ESTADOS_ACTIVOS.contains(estado);
    }

    private Cliente buscarCliente(Long id) {
        return clienteRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No encontramos a ese cliente, elige uno de la lista"));
    }

    private Vehiculo buscarVehiculo(Long id) {
        return vehiculoRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No encontramos ese vehículo, elige uno de la lista"));
    }

    private void pasarDatos(ReservaRequestDTO dto, Reserva reserva, Cliente cliente, Vehiculo vehiculo) {
        reserva.setCliente(cliente);
        reserva.setVehiculo(vehiculo);
        reserva.setNombreReserva(dto.getNombreReserva().trim());
        reserva.setFechaReserva(dto.getFechaReserva());
        reserva.setCantidadPasajeros(dto.getCantidadPasajeros());
        reserva.setCantidadDias(dto.getCantidadDias());

        reserva.setTotalPago(vehiculo.getPrecioDia().multiply(BigDecimal.valueOf(dto.getCantidadDias())));
    }

    private ReservaResponseDTO convertirADTO(Reserva r) {
        ReservaResponseDTO dto = new ReservaResponseDTO();
        dto.setId(r.getId());
        dto.setIdCliente(r.getCliente().getId());
        dto.setNombreCliente(r.getCliente().getNombre() + " " + r.getCliente().getApellido());
        dto.setIdVehiculo(r.getVehiculo().getId());
        dto.setNombreVehiculo(r.getVehiculo().getMarca() + " " + r.getVehiculo().getModelo()
                + " (" + r.getVehiculo().getPlaca() + ")");
        dto.setNombreReserva(r.getNombreReserva());
        dto.setFechaReserva(r.getFechaReserva());
        dto.setCantidadPasajeros(r.getCantidadPasajeros());
        dto.setCantidadDias(r.getCantidadDias());
        dto.setTotalPago(r.getTotalPago());
        dto.setEstado(r.getEstado());
        return dto;
    }
}
