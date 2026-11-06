package fisicas.fuerzas

import fisicas.config.EnvironmentConfig
import fisicas.config.ProjectileConfig
import fisicas.estado.ProjectileState
import fisicas.vector.Vector2D

/**
 * Contrato de abstracción para el cálculo de aceleraciones y fuerzas actuantes sobre un proyectil.
 */
interface CalculadorFuerzas {

    /**
     * Calcula el vector de aceleración total instantánea según las fuerzas ambientales y la dinámica del cuerpo.
     */
    fun calcularAceleracion(estado: ProjectileState, proyectil: ProjectileConfig, entorno: EnvironmentConfig): Vector2D
}
