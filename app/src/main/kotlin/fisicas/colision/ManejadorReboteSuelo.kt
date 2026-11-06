package fisicas.colision

import fisicas.config.BounceConfig
import fisicas.estado.ProjectileState
import fisicas.vector.Vector2D

/**
 * Resuelve el impacto contra la superficie plana del suelo (Y <= 0).
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Responsable exclusivamente de coordinar la resolución
 *   de contacto con el suelo delegando el cálculo de restitución a [BounceConfig].
 */
class ManejadorReboteSuelo : ManejadorColision {

    override fun resolverColision(estadoActual: ProjectileState, configRebote: BounceConfig): ProjectileState {
        if (!estadoActual.estaEnSuelo || estadoActual.estaEnReposo) {
            return estadoActual
        }
        if (estadoActual.velocidad.y >= 0.0) {
            return corregirCotaSuelo(estadoActual)
        }
        val velocidadTentativa = configRebote.calcularVelocidadRebote(estadoActual.velocidad)
        if (configRebote.debeDetenerse(velocidadTentativa)) {
            return estadoActual.detener()
        }
        return estadoActual.copy(posicion = Vector2D(estadoActual.posicion.x, 0.0), velocidad = velocidadTentativa)
    }

    private fun corregirCotaSuelo(estado: ProjectileState): ProjectileState {
        if (estado.posicion.y < 0.0) {
            return estado.conPosicion(Vector2D(estado.posicion.x, 0.0))
        }
        return estado
    }
}
