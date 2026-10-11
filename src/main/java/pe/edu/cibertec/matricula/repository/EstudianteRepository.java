package pe.edu.cibertec.matricula.repository;

import org.springframework.stereotype.Repository;
import pe.edu.cibertec.matricula.model.Estudiante;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EstudianteRepository {

    private final Map<Long, Estudiante> estudiantes = new ConcurrentHashMap<>();

    public EstudianteRepository() {
        estudiantes.put(1001L, new Estudiante(1001L, "Ana Torres Quispe", "Ingenieria de Software", 15.4));
        estudiantes.put(1002L, new Estudiante(1002L, "Carlos Huaman Rojas", "Computacion e Informatica", 9.8));
    }

    public Optional<Estudiante> buscarPorId(Long estudianteId) {
        return Optional.ofNullable(estudiantes.get(estudianteId));
    }
}
