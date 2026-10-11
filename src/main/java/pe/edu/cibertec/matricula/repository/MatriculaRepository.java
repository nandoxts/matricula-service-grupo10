package pe.edu.cibertec.matricula.repository;

import org.springframework.stereotype.Repository;
import pe.edu.cibertec.matricula.model.DetalleMatricula;
import pe.edu.cibertec.matricula.model.Matricula;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repositorio en memoria con las matriculas semilla.
 */
@Repository
public class MatriculaRepository {

    private final Map<Long, Matricula> matriculas = new ConcurrentHashMap<>();

    public MatriculaRepository() {
        DetalleMatricula daw = new DetalleMatricula(1L, "DAW201", "Desarrollo de Aplicaciones Web II", "T6AA", 4, 120.00);
        DetalleMatricula bda = new DetalleMatricula(2L, "BDA201", "Base de Datos Avanzada", "T6AB", 3, 120.00);
        DetalleMatricula arq = new DetalleMatricula(3L, "ARQ301", "Arquitectura de Software", "T6AC", 4, 120.00);
        DetalleMatricula mov = new DetalleMatricula(4L, "MOV201", "Desarrollo de Aplicaciones Moviles", "T6AD", 3, 120.00);

        guardar(new Matricula(1L, "MAT-2026-0001", LocalDateTime.of(2026, 10, 10, 9, 30),
                1001L, "2026-II", "EN_PROCESO", List.of(daw, bda)));
        guardar(new Matricula(2L, "MAT-2026-0002", LocalDateTime.of(2026, 10, 10, 10, 15),
                1002L, "2026-II", "EN_PROCESO", List.of(daw, arq)));
        guardar(new Matricula(3L, "MAT-2026-0003", LocalDateTime.of(2026, 3, 15, 8, 0),
                1001L, "2026-I", "CONFIRMADA", List.of(mov)));
    }

    public Optional<Matricula> buscarPorId(Long idMatricula) {
        return Optional.ofNullable(matriculas.get(idMatricula));
    }

    public List<Matricula> listarPorEstudiante(Long estudianteId) {
        return matriculas.values().stream()
                .filter(m -> m.getEstudianteId().equals(estudianteId))
                .sorted(Comparator.comparing(Matricula::getIdMatricula))
                .toList();
    }

    public Matricula guardar(Matricula matricula) {
        matriculas.put(matricula.getIdMatricula(), matricula);
        return matricula;
    }
}
