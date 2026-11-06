package fisicas.fuerzas

import fisicas.config.EnvironmentConfig
import fisicas.config.ProjectileConfig
import fisicas.estado.ProjectileState
import fisicas.vector.Vector2D

/**
 * Implementación de la dinámica de Newton considerando arrastre aerodinámico lineal y fuerza eólica.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Responsable exclusivamente de aplicar las leyes de Newton
 *   para calcular la aceleración neta a partir de las fuerzas del modelo.
 */
class CalculadorFuerzasNewton : CalculadorFuerzas {

    override fun calcularAceleracion(estado: ProjectileState, proyectil: ProjectileConfig, entorno: EnvironmentConfig): Vector2D {
        val fDrag = proyectil.calcularFuerzaArrastre(estado.velocidad)
        val fViento = entorno.viento.aVector()
        val ax = (fDrag.x + fViento.x) / proyectil.masa
        val ay = -entorno.obtenerGravedad() + (fDrag.y + fViento.y) / proyectil.masa
        return Vector2D(ax, ay)
    }
}
