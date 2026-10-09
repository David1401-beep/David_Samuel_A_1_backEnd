package SystemITR.JosueGuinea1A.modules.vehiculos.repository;

import SystemITR.JosueGuinea1A.modules.vehiculos.model.entity.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
}
