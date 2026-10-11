package pe.edu.cibertec.matricula.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.cibertec.matricula.dto.MatriculaResponseDTO;
import pe.edu.cibertec.matricula.dto.ResumenFichaEstudianteDTO;
import pe.edu.cibertec.matricula.service.MatriculaService;

@RestController
@RequestMapping("/api/v1/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;

    @GetMapping("/{idMatricula}")
    public MatriculaResponseDTO obtenerMatricula(@PathVariable("idMatricula") Long idMatricula) {
        return matriculaService.obtenerMatricula(idMatricula);
    }

    @GetMapping("/validar-y-confirmar/{idMatricula}")
    public MatriculaResponseDTO validarYConfirmar(@PathVariable("idMatricula") Long idMatricula) {
        return matriculaService.validarYConfirmar(idMatricula);
    }

    @GetMapping("/ficha-estudiante/{estudianteId}")
    public ResumenFichaEstudianteDTO obtenerFichaEstudiante(@PathVariable("estudianteId") Long estudianteId) {
        return matriculaService.obtenerFichaEstudiante(estudianteId);
    }
}
