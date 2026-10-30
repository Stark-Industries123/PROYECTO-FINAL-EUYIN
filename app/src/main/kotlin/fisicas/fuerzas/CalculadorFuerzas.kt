package fisicas.fuerzas

import fisicas.config.EnvironmentConfig
import fisicas.config.ProjectileConfig
import fisicas.estado.ProjectileState
import fisicas.vector.Vector2D

/**
 * Contrato de abstracción para el cálculo de aceleraciones y fuerzas actuantes sobre un proyectil.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Dependency Inversion Principle (DIP): Los motores de integración dependen de esta abstracción.
 * - Open/Closed Principle (OCP): Permite extender a nuevos modelos de resistencia (ej. cuadrática, Stokes, Coriolis)
 *   sin modificar las clases de integración ni de estado.
 */
interface CalculadorFuerzas {

    /**
     * Calcula el vector de aceleración total instantánea según las fuerzas ambientales y la dinámica del cuerpo.
     *
     * @param estado Estado cinemático instantáneo del proyectil (posición, velocidad).
     * @param proyectil Configuración física del proyectil (masa, arrastre k).
     * @param entorno Configuración del entorno (fuente de gravedad, viento).
     * @return [Vector2D] con las aceleraciones (ax, ay) en m/s².
     */
    fun calcularAceleracion(
        estado: ProjectileState,
        proyectil: ProjectileConfig,
        entorno: EnvironmentConfig
    ): Vector2D
}
