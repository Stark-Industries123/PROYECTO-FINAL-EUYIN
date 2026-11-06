package fisicas.motor

import fisicas.config.EnvironmentConfig
import fisicas.config.ProjectileConfig
import fisicas.estado.ProjectileState

/**
 * Contrato de abstracción para el motor de física y simulación cinemática.
 */
interface PhysicsEngine {

    /**
     * Calcula la evolución temporal del proyectil para un diferencial de tiempo dt.
     */
    fun actualizarPaso(estado: ProjectileState, proyectil: ProjectileConfig, entorno: EnvironmentConfig, dt: Double): ProjectileState

    /**
     * Simula la trayectoria completa del proyectil desde el estado inicial hasta el reposo o tiempo límite.
     */
    fun simular(estadoInicial: ProjectileState, proyectil: ProjectileConfig, entorno: EnvironmentConfig, dt: Double = DEFAULT_DT): List<ProjectileState>

    companion object {
        const val DEFAULT_DT: Double = 0.01
        const val DEFAULT_MAX_TIEMPO: Double = 300.0
    }
}
