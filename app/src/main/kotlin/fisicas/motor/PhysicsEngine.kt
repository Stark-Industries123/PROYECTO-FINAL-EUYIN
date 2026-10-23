package fisicas.motor

import fisicas.config.EnvironmentConfig
import fisicas.config.ProjectileConfig
import fisicas.estado.ProjectileState

/**
 * Contrato de abstracción para el motor de física y simulación cinemática.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Dependency Inversion Principle (DIP): Los módulos de alto nivel dependen de esta interfaz.
 * - Open/Closed Principle (OCP): Abierto a diferentes algoritmos de integración temporal (Euler-Cromer, Verlet, RK4).
 */
interface PhysicsEngine {

    /**
     * Calcula la evolución temporal del proyectil para un diferencial de tiempo dt.
     *
     * @param estado Estado cinemático actual.
     * @param proyectil Parámetros físicos del proyectil.
     * @param entorno Parámetros del entorno físico.
     * @param dt Intervalo temporal de integración en segundos.
     * @return Nuevo [ProjectileState] evolucionado.
     */
    fun actualizarPaso(
        estado: ProjectileState,
        proyectil: ProjectileConfig,
        entorno: EnvironmentConfig,
        dt: Double
    ): ProjectileState

    /**
     * Simula la trayectoria completa del proyectil desde el estado inicial hasta el reposo o tiempo límite.
     *
     * @param estadoInicial Estado de partida en t = 0.
     * @param proyectil Parámetros físicos del proyectil.
     * @param entorno Parámetros del entorno físico.
     * @param dt Paso temporal de integración.
     * @param tiempoMaximo Límite temporal de seguridad.
     * @return Lista inmutable con la serie de estados [ProjectileState].
     */
    fun simular(
        estadoInicial: ProjectileState,
        proyectil: ProjectileConfig,
        entorno: EnvironmentConfig,
        dt: Double = DEFAULT_DT,
        tiempoMaximo: Double = DEFAULT_MAX_TIEMPO
    ): List<ProjectileState>

    companion object {
        const val DEFAULT_DT: Double = 0.01
        const val DEFAULT_MAX_TIEMPO: Double = 300.0
    }
}
