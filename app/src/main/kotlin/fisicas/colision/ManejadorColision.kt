package fisicas.colision

import fisicas.config.BounceConfig
import fisicas.config.EnvironmentConfig
import fisicas.estado.ProjectileState

/**
 * Contrato de abstracción para la detección y resolución de colisiones del proyectil.
 */
interface ManejadorColision {

    /**
     * Evalúa si el proyectil ha impactado contra una superficie y aplica las leyes de restitución y fricción.
     */
    fun resolverColision(estadoActual: ProjectileState, configRebote: BounceConfig): ProjectileState

    /**
     * Sobrecarga de conveniencia que resuelve la colisión a partir del [EnvironmentConfig].
     */
    fun resolverColision(estadoActual: ProjectileState, entorno: EnvironmentConfig): ProjectileState = resolverColision(estadoActual, entorno.rebote)
}
