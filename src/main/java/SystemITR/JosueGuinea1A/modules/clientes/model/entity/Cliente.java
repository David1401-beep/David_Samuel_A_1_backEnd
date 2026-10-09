package SystemITR.JosueGuinea1A.modules.clientes.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "CLIENTES")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENTE")
    private Long id;

    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "APELLIDO")
    private String apellido;
    @Column(name = "TELEFONO")
    private String telefono;
    @Column(name = "EMAIL", unique = true)
    private String email;
    @Column(name = "DIRECCION")
    private String direccion;
}
