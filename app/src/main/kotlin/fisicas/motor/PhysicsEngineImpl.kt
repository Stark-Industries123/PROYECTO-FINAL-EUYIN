package fisicas.motor

import fisicas.colision.ManejadorColision
import fisicas.config.EnvironmentConfig
import fisicas.config.ProjectileConfig
import fisicas.estado.ProjectileState
import fisicas.fuerzas.CalculadorFuerzas

/**
 * Motor de física que integra las ecuaciones de movimiento mediante el método de Euler-Cromer
 * e incorpora cálculo de fuerzas desacoplado y resolución de impacto contra el suelo.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Responsable únicamente de coordinar el avance temporal,
 *   delegando el cálculo de fuerzas en [CalculadorFuerzas] y el impacto en [ManejadorColision].
 */
class PhysicsEngineImpl(
    private val calculadorFuerzas: CalculadorFuerzas,
    private val manejadorColision: ManejadorColision,
    private val tiempoMaximo: Double = 300.0
) : PhysicsEngine {

    init {
        require(tiempoMaximo > 0.0) { "El tiempo máximo de simulación debe ser positivo (recibido: $tiempoMaximo)." }
    }

    override fun actualizarPaso(estado: ProjectileState, proyectil: ProjectileConfig, entorno: EnvironmentConfig, dt: Double): ProjectileState {
        require(dt > 0.0 && tiempoMaximo > 0.0) { "El paso temporal dt debe ser estrictamente positivo (recibido: $dt)." }
        if (estado.estaEnReposo) return estado
        val aceleracion = calculadorFuerzas.calcularAceleracion(estado, proyectil, entorno)
        val nuevaVelocidad = estado.velocidad + (aceleracion * dt)
        val nuevaPosicion = estado.posicion + (nuevaVelocidad * dt)
        val estadoTentativo = estado.avanzar(nuevaPosicion, nuevaVelocidad, dt)
        return manejadorColision.resolverColision(estadoTentativo, entorno)
    }

    override fun simular(estadoInicial: ProjectileState, proyectil: ProjectileConfig, entorno: EnvironmentConfig, dt: Double): List<ProjectileState> {
        require(dt > 0.0) { "El paso temporal dt debe ser estrictamente positivo (recibido: $dt)." }
        val trayectoria = mutableListOf(estadoInicial)
        var estadoActual = estadoInicial
        while (!estadoActual.estaEnReposo && estadoActual.tiempo < tiempoMaximo) {
            estadoActual = actualizarPaso(estadoActual, proyectil, entorno, dt)
            trayectoria.add(estadoActual)
        }
        return trayectoria.toList()
    }
}
