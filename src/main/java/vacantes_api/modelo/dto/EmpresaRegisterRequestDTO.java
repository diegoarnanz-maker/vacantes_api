package vacantes_api.modelo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaRegisterRequestDTO {

    //Desde el frontend el admin rellenara un formulario unico con los datos de la empresa y del usuario, por lo que se necesita un DTO que contenga todos los datos

    // Datos del usuario
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellidos;

    // Datos de la empresa
    @NotBlank
    private String nombreEmpresa;

    @NotBlank
    private String cif;

    @NotBlank
    private String direccionFiscal;

    @NotBlank
    private String pais;
}
