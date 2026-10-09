package SystemITR.JosueGuinea1A.modules.reservas.repository;

import SystemITR.JosueGuinea1A.modules.reservas.model.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findAllByOrderByFechaReservaDesc();

    List<Reserva> findByVehiculoIdAndFechaReservaAndEstadoIn(Long idVehiculo, LocalDate fecha, List<String> estados);
}
