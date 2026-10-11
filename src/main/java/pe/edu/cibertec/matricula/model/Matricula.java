package pe.edu.cibertec.matricula.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidad interna de matricula. Los totales (creditos y pension) no se guardan,
 * se calculan en cada consulta para que nunca queden desactualizados.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {

    private Long idMatricula;
    private String codigoMatricula;
    private LocalDateTime fechaMatricula;
    private Long estudianteId;
    private String periodoAcademico;
    private String estadoMatricula;
    private List<DetalleMatricula> detalles;
}
