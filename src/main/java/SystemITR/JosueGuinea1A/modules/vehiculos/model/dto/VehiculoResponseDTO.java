package SystemITR.JosueGuinea1A.modules.vehiculos.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VehiculoResponseDTO {
    private Long id;
    private String marca;
    private String modelo;
    private Integer capacidad;
    private BigDecimal precioDia;
    private String placa;
}
