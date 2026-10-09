package SystemITR.JosueGuinea1A.modules.clientes.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteRequestDTO {
    @NotBlank(message = "Escribe el nombre del cliente")
    @Size(max = 100, message = "El nombre es muy largo, máximo 100 caracteres")
    private String nombre;

    @NotBlank(message = "Escribe el apellido del cliente")
    @Size(max = 100, message = "El apellido es muy largo, máximo 100 caracteres")
    private String apellido;

    @NotBlank(message = "Escribe el teléfono del cliente")
    @Size(max = 15, message = "El teléfono es muy largo, máximo 15 caracteres")
    private String telefono;

    @NotBlank(message = "Escribe el correo del cliente")
    @Email(regexp = "^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$", message = "Ese correo no parece válido, revísalo")
    @Size(max = 100, message = "El correo es muy largo, máximo 100 caracteres")
    private String email;

    @Size(max = 200, message = "La dirección es muy larga, máximo 200 caracteres")
    private String direccion;
}
