package SystemITR.JosueGuinea1A.modules.vehiculos.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter @Setter
@Table(name = "VEHICULOS")
public class Vehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_VEHICULO")
    private Long id;

    @Column(name = "MARCA")
    private String marca;
    @Column(name = "MODELO")
    private String modelo;
    @Column(name = "CAPACIDAD")
    private Integer capacidad;
    @Column(name = "PRECIO_DIA")
    private BigDecimal precioDia;
    @Column(name = "PLACA", unique = true)
    private String placa;
}
