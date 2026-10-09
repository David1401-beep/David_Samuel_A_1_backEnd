package SystemITR.JosueGuinea1A.modules.reservas.model.entity;

import SystemITR.JosueGuinea1A.modules.clientes.model.entity.Cliente;
import SystemITR.JosueGuinea1A.modules.vehiculos.model.entity.Vehiculo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter @Setter
@Table(name = "RESERVAS")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESERVA")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "ID_VEHICULO")
    private Vehiculo vehiculo;

    @Column(name = "NOMBRE_RESERVA")
    private String nombreReserva;
    @Column(name = "FECHA_RESERVA")
    private LocalDate fechaReserva;
    @Column(name = "CANTIDAD_PASAJEROS")
    private Integer cantidadPasajeros;
    @Column(name = "CANTIDAD_DIAS")
    private Integer cantidadDias;
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "TOTAL_PAGO")
    private BigDecimal totalPago;
}
