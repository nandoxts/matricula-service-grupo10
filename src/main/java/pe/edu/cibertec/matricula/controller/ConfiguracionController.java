package pe.edu.cibertec.matricula.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.matricula.dto.ConfiguracionSedeDTO;

/**
 * Expone la configuracion de la sede leida desde el Config Server.
 * Con @RefreshScope el bean se recrea tras POST /actuator/refresh, sin reiniciar el servicio.
 */
@RefreshScope
@RestController
@RequestMapping("/api/v1/matriculas")
public class ConfiguracionController {

    @Value("${edusmart.sede}")
    private String sede;

    @Value("${edusmart.costo-credito}")
    private Double costoCredito;

    @Value("${edusmart.max-creditos-ciclo}")
    private Integer maxCreditosCiclo;

    @Value("${edusmart.mensaje-bienvenida}")
    private String mensajeBienvenida;

    @Value("${edusmart.limite-cuotas-pago}")
    private Integer limiteCuotasPago;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @GetMapping("/configuracion")
    public ConfiguracionSedeDTO obtenerConfiguracion() {
        return ConfiguracionSedeDTO.builder()
                .sede(sede)
                .costoCredito(costoCredito)
                .maxCreditosCiclo(maxCreditosCiclo)
                .mensajeBienvenida(mensajeBienvenida)
                .limiteCuotasPago(limiteCuotasPago)
                .datasourceUrl(datasourceUrl)
                .build();
    }
}
