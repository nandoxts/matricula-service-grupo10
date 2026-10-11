# matricula-service-grupo10

> **EduSmart Campus – Microservicios con Spring Cloud** · Laboratorio T2 (Fila B) · Grupo 10
> Java 25 · Spring Boot 4.1.1 · Spring Cloud 2025.1.3 · Maven

Microservicio de **matrículas**: inscripción de asignaturas, cálculo de créditos y pensión, y Ficha Consolidada del Estudiante (Pregunta 1). Consume a `curso-service-grupo10` con `@FeignClient` resuelto por Eureka.

| Dato | Valor |
|---|---|
| Puerto | `8085` |
| Nombre en Eureka | `MATRICULA-SERVICE-GRUPO10` |
| Dependencias | Spring Web, Eureka Discovery Client, OpenFeign, Lombok |

## Endpoints

| Método | URL | Respuesta |
|---|---|---|
| GET | `/api/v1/matriculas/{idMatricula}` | `MatriculaResponseDTO` |
| GET | `/api/v1/matriculas/validar-y-confirmar/{idMatricula}` | `MatriculaResponseDTO` (Feign por cada curso) |
| GET | `/api/v1/matriculas/ficha-estudiante/{estudianteId}` | `ResumenFichaEstudianteDTO` (Feign) |

## Reglas de negocio

- `subtotalCurso = creditos × costoPorCredito`
- `totalCreditosInscritos = Σ creditos` · `montoTotalPension = Σ subtotalCurso` (calculados en cada consulta)
- Confirmación: todos los cursos con `vacantesDisponibles > 0` y `estadoCurso == "ABIERTO"` → `CONFIRMADA`; si no → `RECHAZADA_SIN_VACANTES`
- Situación académica: promedio `>= 11.0` → `REGULAR`; si no → `OBSERVADO`

## Ejecutar

```bash
./mvnw spring-boot:run      # requiere discovery-server y curso-service
```
