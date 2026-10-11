package pe.edu.cibertec.matricula.service;

import pe.edu.cibertec.matricula.dto.MatriculaResponseDTO;
import pe.edu.cibertec.matricula.dto.ResumenFichaEstudianteDTO;

public interface MatriculaService {

    MatriculaResponseDTO obtenerMatricula(Long idMatricula);

    MatriculaResponseDTO validarYConfirmar(Long idMatricula);

    ResumenFichaEstudianteDTO obtenerFichaEstudiante(Long estudianteId);
}
