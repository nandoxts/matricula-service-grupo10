package pe.edu.cibertec.matricula.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contrato recibido desde curso-service via Feign (replicado porque cada servicio es un repositorio independiente).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CursoDisponibilidadDTO {

    private Long idCurso;
    private String codigoCurso;
    private String nombreCurso;
    private Integer vacantesDisponibles;
    private String prerrequisito;
    private String estadoCurso;
}
