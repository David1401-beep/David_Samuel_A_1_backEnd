package SystemITR.JosueGuinea1A.modules.reservas.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservaRequestDTO {
    @NotNull(message = "Elige el cliente que hace la reserva")
    private Long idCliente;

    @NotNull(message = "Elige el vehículo que quieres reservar")
    private Long idVehiculo;

    @NotBlank(message = "Ponle un nombre a la reserva")
    @Size(max = 100, message = "El nombre de la reserva es muy largo, máximo 100 caracteres")
    private String nombreReserva;

    @NotNull(message = "Indica la fecha de la reserva")
    private LocalDate fechaReserva;

    @NotNull(message = "Indica cuántos pasajeros van a viajar")
    @Positive(message = "Tiene que viajar al menos un pasajero")
    private Integer cantidadPasajeros;

    @NotNull(message = "Indica por cuántos días se renta el vehículo")
    @Positive(message = "La renta debe ser de al menos un día")
    private Integer cantidadDias;
}
