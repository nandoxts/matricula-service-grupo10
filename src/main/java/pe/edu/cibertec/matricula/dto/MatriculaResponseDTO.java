package pe.edu.cibertec.matricula.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaResponseDTO {

    private Long idMatricula;
    private String codigoMatricula;
    private LocalDateTime fechaMatricula;
    private Long estudianteId;
    private String periodoAcademico;
    private String estadoMatricula;
    private List<DetalleCursoMatriculadoDTO> listaCursosMatriculados;
    private Integer totalCreditosInscritos;
    private Double montoTotalPension;
}
