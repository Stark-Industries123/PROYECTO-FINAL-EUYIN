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
 * - Liskov Substitution Principle (LSP): Cumple completamente el contrato [CalculadorFuerzas].
 *
 * Ecuaciones implementadas:
 * - F_drag = -k * v  => (Fdrag_x = -k * vx, Fdrag_y = -k * vy)
 * - F_viento = viento.aVector()
 * - ax = (Fdrag_x + Fviento_x) / m
 * - ay = -gravedad + (Fdrag_y + Fviento_y) / m
 */
class CalculadorFuerzasNewton : CalculadorFuerzas {

    override fun calcularAceleracion(
        estado: ProjectileState,
        proyectil: ProjectileConfig,
        entorno: EnvironmentConfig
    ): Vector2D {
        val m = proyectil.masa
        val k = proyectil.coeficienteArrastre
        val gravedad = entorno.fuenteGravedad.gravedad

        // Fuerza de arrastre opuesta a la velocidad: F_drag = -k * v
        val fDrag = estado.velocidad * (-k)

        // Vector de fuerza eólica
        val fViento = entorno.viento.aVector()

        // Segunda Ley de Newton: ax = (Fdrag_x + Fviento_x) / m
        val ax = (fDrag.x + fViento.x) / m

        // ay = -gravedad + (Fdrag_y + Fviento_y) / m
        val ay = -gravedad + (fDrag.y + fViento.y) / m

        return Vector2D(ax, ay)
    }
}
