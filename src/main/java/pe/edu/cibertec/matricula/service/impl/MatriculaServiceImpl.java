package pe.edu.cibertec.matricula.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.matricula.client.CursoClient;
import pe.edu.cibertec.matricula.dto.CursoDisponibilidadDTO;
import pe.edu.cibertec.matricula.dto.DetalleCursoMatriculadoDTO;
import pe.edu.cibertec.matricula.dto.MatriculaResponseDTO;
import pe.edu.cibertec.matricula.dto.ResumenFichaEstudianteDTO;
import pe.edu.cibertec.matricula.exception.RecursoNoEncontradoException;
import pe.edu.cibertec.matricula.model.DetalleMatricula;
import pe.edu.cibertec.matricula.model.Estudiante;
import pe.edu.cibertec.matricula.model.Matricula;
import pe.edu.cibertec.matricula.repository.EstudianteRepository;
import pe.edu.cibertec.matricula.repository.MatriculaRepository;
import pe.edu.cibertec.matricula.service.MatriculaService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatriculaServiceImpl implements MatriculaService {

    public static final String ESTADO_CONFIRMADA = "CONFIRMADA";
    public static final String ESTADO_RECHAZADA = "RECHAZADA_SIN_VACANTES";
    public static final String CURSO_ABIERTO = "ABIERTO";
    public static final String SITUACION_REGULAR = "REGULAR";
    public static final String SITUACION_OBSERVADO = "OBSERVADO";
    /** Nota aprobatoria en escala vigesimal (RN-05). */
    public static final double PROMEDIO_MINIMO_REGULAR = 11.0;

    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final CursoClient cursoClient;
    private final Environment environment;

    @Override
    public MatriculaResponseDTO obtenerMatricula(Long idMatricula) {
        return toResponseDTO(buscarMatricula(idMatricula));
    }

    @Override
    public MatriculaResponseDTO validarYConfirmar(Long idMatricula) {
        Matricula matricula = buscarMatricula(idMatricula);

        // RN-04: todos los cursos deben tener vacantes y estar ABIERTOS
        boolean todosDisponibles = matricula.getDetalles().stream()
                .allMatch(detalle -> cursoDisponible(detalle.getIdCurso()));

        matricula.setEstadoMatricula(todosDisponibles ? ESTADO_CONFIRMADA : ESTADO_RECHAZADA);
        return toResponseDTO(matriculaRepository.guardar(matricula));
    }

    @Override
    public ResumenFichaEstudianteDTO obtenerFichaEstudiante(Long estudianteId) {
        Estudiante estudiante = estudianteRepository.buscarPorId(estudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el estudiante con id " + estudianteId));

        List<MatriculaResponseDTO> historial = matriculaRepository.listarPorEstudiante(estudianteId).stream()
                .map(this::toResponseDTO)
                .toList();

        return ResumenFichaEstudianteDTO.builder()
                .estudianteId(estudiante.getEstudianteId())
                .nombreEstudiante(estudiante.getNombreEstudiante())
                .carrera(estudiante.getCarrera())
                .sede(environment.getProperty("edusmart.sede", "Sede no configurada")) // RN-07
                .totalMatriculasRealizadas(historial.size())                           // RN-06
                .promedioPonderadoAcumulado(estudiante.getPromedioPonderadoAcumulado())
                .historialMatriculas(historial)
                .catalogoCursosElegibles(obtenerCursosElegibles(estudianteId))
                .situacionAcademica(estudiante.getPromedioPonderadoAcumulado() >= PROMEDIO_MINIMO_REGULAR
                        ? SITUACION_REGULAR : SITUACION_OBSERVADO)                    // RN-05
                .build();
    }

    private Matricula buscarMatricula(Long idMatricula) {
        return matriculaRepository.buscarPorId(idMatricula)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la matricula con id " + idMatricula));
    }

    /** Consulta via Feign; si curso-service responde 404 o no esta disponible, el curso se considera no valido. */
    private boolean cursoDisponible(Long idCurso) {
        try {
            CursoDisponibilidadDTO curso = cursoClient.obtenerCurso(idCurso);
            return curso != null
                    && curso.getVacantesDisponibles() != null
                    && curso.getVacantesDisponibles() > 0
                    && CURSO_ABIERTO.equals(curso.getEstadoCurso());
        } catch (FeignException ex) {
            log.warn("No se pudo validar el curso {} en curso-service: {}", idCurso, ex.getMessage());
            return false;
        }
    }

    private List<CursoDisponibilidadDTO> obtenerCursosElegibles(Long estudianteId) {
        try {
            return cursoClient.listarCursosPorEstudiante(estudianteId);
        } catch (FeignException ex) {
            log.warn("No se pudo obtener el catalogo del estudiante {}: {}", estudianteId, ex.getMessage());
            return List.of();
        }
    }

    private MatriculaResponseDTO toResponseDTO(Matricula matricula) {
        List<DetalleCursoMatriculadoDTO> detalles = matricula.getDetalles().stream()
                .map(this::toDetalleDTO)
                .toList();

        // RN-02 y RN-03: totales calculados en cada consulta
        int totalCreditos = detalles.stream().mapToInt(DetalleCursoMatriculadoDTO::getCreditos).sum();
        double totalPension = redondear(detalles.stream().mapToDouble(DetalleCursoMatriculadoDTO::getSubtotalCurso).sum());

        return MatriculaResponseDTO.builder()
                .idMatricula(matricula.getIdMatricula())
                .codigoMatricula(matricula.getCodigoMatricula())
                .fechaMatricula(matricula.getFechaMatricula())
                .estudianteId(matricula.getEstudianteId())
                .periodoAcademico(matricula.getPeriodoAcademico())
                .estadoMatricula(matricula.getEstadoMatricula())
                .listaCursosMatriculados(detalles)
                .totalCreditosInscritos(totalCreditos)
                .montoTotalPension(totalPension)
                .build();
    }

    private DetalleCursoMatriculadoDTO toDetalleDTO(DetalleMatricula detalle) {
        return DetalleCursoMatriculadoDTO.builder()
                .idCurso(detalle.getIdCurso())
                .codigoCurso(detalle.getCodigoCurso())
                .nombreCurso(detalle.getNombreCurso())
                .seccion(detalle.getSeccion())
                .creditos(detalle.getCreditos())
                .costoPorCredito(detalle.getCostoPorCredito())
                .subtotalCurso(redondear(detalle.getCreditos() * detalle.getCostoPorCredito())) // RN-01
                .build();
    }

    private double redondear(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
