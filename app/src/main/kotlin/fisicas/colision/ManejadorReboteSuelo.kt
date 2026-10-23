package fisicas.colision

import fisicas.config.BounceConfig
import fisicas.estado.ProjectileState
import fisicas.vector.Vector2D
import kotlin.math.hypot

/**
 * Resuelve el impacto contra la superficie plana del suelo (Y <= 0).
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Responsable exclusivamente de la cinemática de rebote y detención por fricción/umbral.
 * - Inmutabilidad (POO): Retorna nuevos estados inmutables sin modificar los argumentos de entrada.
 *
 * Reglas de física:
 * - Se activa cuando y <= 0 y la velocidad vertical vy < 0 (dirigida hacia el suelo).
 * - Vy_nueva = -e * Vy_actual (restitución).
 * - Vx_nueva = Vx_actual * friccion_suelo.
 * - Si ||V_nueva|| < umbralReposo: se detiene (Vx = 0, Vy = 0, y = 0, enReposo = true).
 * - La posición Y queda restringida a 0.0 para evitar hundimiento.
 */
class ManejadorReboteSuelo : ManejadorColision {

    override fun resolverColision(
        estadoActual: ProjectileState,
        configRebote: BounceConfig
    ): ProjectileState {
        // Si no ha tocado el suelo o ya está en reposo, no hay impacto que procesar
        if (!estadoActual.estaEnSuelo || estadoActual.estaEnReposo) {
            return estadoActual
        }

        // Si la componente vertical no va hacia abajo (por ejemplo ya está rebotando hacia arriba), solo corregimos cota Y
        if (estadoActual.velocidad.y >= 0.0) {
            return if (estadoActual.posicion.y < 0.0) {
                estadoActual.conPosicion(Vector2D(estadoActual.posicion.x, 0.0))
            } else {
                estadoActual
            }
        }

        // Aplicamos restitución vertical y fricción horizontal
        val vyNueva = -configRebote.coeficienteRestitucion * estadoActual.velocidad.y
        val vxNueva = estadoActual.velocidad.x * configRebote.friccionSuelo
        val velocidadTentativa = Vector2D(vxNueva, vyNueva)

        // Verificamos si la rapidez total cae por debajo del umbral mínimo (epsilon)
        val rapidezTotal = velocidadTentativa.magnitud()
        return if (rapidezTotal < configRebote.umbralReposo) {
            estadoActual.detener()
        } else {
            estadoActual.copy(
                posicion = Vector2D(estadoActual.posicion.x, 0.0),
                velocidad = velocidadTentativa
            )
        }
    }
}
