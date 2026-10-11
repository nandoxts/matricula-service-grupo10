package pe.edu.cibertec.matricula.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Valores de la sede activa leidos desde el Config Server.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionSedeDTO {

    private String sede;
    private Double costoCredito;
    private Integer maxCreditosCiclo;
    private String mensajeBienvenida;
    private Integer limiteCuotasPago;
    private String datasourceUrl;
}
