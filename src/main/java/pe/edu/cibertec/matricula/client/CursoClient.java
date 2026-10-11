package pe.edu.cibertec.matricula.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pe.edu.cibertec.matricula.dto.CursoDisponibilidadDTO;

import java.util.List;

/**
 * Cliente declarativo hacia curso-service. El name es el registrado en Eureka,
 * asi que no hay URLs fijas: Spring Cloud LoadBalancer resuelve host y puerto.
 */
@FeignClient(name = "curso-service-grupo10")
public interface CursoClient {

    @GetMapping("/api/v1/cursos/{idCurso}")
    CursoDisponibilidadDTO obtenerCurso(@PathVariable("idCurso") Long idCurso);

    @GetMapping("/api/v1/cursos/estudiante/{estudianteId}")
    List<CursoDisponibilidadDTO> listarCursosPorEstudiante(@PathVariable("estudianteId") Long estudianteId);
}
