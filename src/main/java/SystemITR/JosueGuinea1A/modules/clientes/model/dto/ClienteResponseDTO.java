package SystemITR.JosueGuinea1A.modules.clientes.model.dto;

import lombok.Data;

@Data
public class ClienteResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String direccion;
}
