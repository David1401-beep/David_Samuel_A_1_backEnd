package SystemITR.JosueGuinea1A.modules.reservas.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ReservaResponseDTO {
    private Long id;
    private Long idCliente;
    private String nombreCliente;
    private Long idVehiculo;
    private String nombreVehiculo;
    private String nombreReserva;
    private LocalDate fechaReserva;
    private Integer cantidadPasajeros;
    private Integer cantidadDias;
    private BigDecimal totalPago;
    private String estado;
}
