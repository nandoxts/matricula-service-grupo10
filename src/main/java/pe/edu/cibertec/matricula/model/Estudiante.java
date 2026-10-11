package pe.edu.cibertec.matricula.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Estudiante {

    private Long estudianteId;
    private String nombreEstudiante;
    private String carrera;
    private Double promedioPonderadoAcumulado;
}
