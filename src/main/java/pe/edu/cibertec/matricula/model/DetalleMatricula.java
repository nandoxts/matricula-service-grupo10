package pe.edu.cibertec.matricula.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Curso inscrito dentro de una matricula. El subtotal no se guarda: se calcula en el servicio.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleMatricula {

    private Long idCurso;
    private String codigoCurso;
    private String nombreCurso;
    private String seccion;
    private Integer creditos;
    private Double costoPorCredito;
}
