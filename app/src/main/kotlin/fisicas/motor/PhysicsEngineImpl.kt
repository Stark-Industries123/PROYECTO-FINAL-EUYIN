package fisicas.motor

import fisicas.colision.ManejadorColision
import fisicas.colision.ManejadorReboteSuelo
import fisicas.config.EnvironmentConfig
import fisicas.config.ProjectileConfig
import fisicas.estado.ProjectileState
import fisicas.fuerzas.CalculadorFuerzas
import fisicas.fuerzas.CalculadorFuerzasNewton

/**
 * Motor de física que integra las ecuaciones de movimiento mediante el método de Euler-Cromer
 * e incorpora cálculo de fuerzas desacoplado y resolución de impacto contra el suelo.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Responsable únicamente de coordinar el avance temporal,
 *   delegando el cálculo de fuerzas en [CalculadorFuerzas] y el impacto en [ManejadorColision].
 * - Dependency Inversion Principle (DIP): Depende exclusivamente de abstracciones inyectadas por constructor.
 * - Open/Closed Principle (OCP): El comportamiento se extiende inyectando colaboradores alternativos
 *   sin modificar esta clase.
 *
 * @property calculadorFuerzas Estrategia encargada de determinar la aceleración neta según la 2ª Ley de Newton.
 * @property manejadorColision Estrategia encargada de procesar el contacto con el suelo y rebotes.
 */
class PhysicsEngineImpl(
    private val calculadorFuerzas: CalculadorFuerzas = CalculadorFuerzasNewton(),
    private val manejadorColision: ManejadorColision = ManejadorReboteSuelo()
) : PhysicsEngine {

    override fun actualizarPaso(
        estado: ProjectileState,
        proyectil: ProjectileConfig,
        entorno: EnvironmentConfig,
        dt: Double
    ): ProjectileState {
        require(dt > 0.0) { "El paso temporal dt debe ser estrictamente positivo (recibido: $dt)." }

        if (estado.estaEnReposo) {
            return estado
        }

        // 1. Cálculo de aceleración neta (F_drag + F_viento + Gravedad)
        val aceleracion = calculadorFuerzas.calcularAceleracion(estado, proyectil, entorno)

        // 2. Integración semi-implícita (Euler-Cromer: actualiza velocidad primero, luego posición)
        val nuevaVelocidad = estado.velocidad + (aceleracion * dt)
        val nuevaPosicion = estado.posicion + (nuevaVelocidad * dt)

        val estadoTentativo = estado.avanzar(
            nuevaPosicion = nuevaPosicion,
            nuevaVelocidad = nuevaVelocidad,
            deltaTiempo = dt
        )

        // 3. Resolución de impacto con el suelo (restitución, fricción, reposo)
        return manejadorColision.resolverColision(estadoTentativo, entorno.rebote)
    }

    override fun simular(
        estadoInicial: ProjectileState,
        proyectil: ProjectileConfig,
        entorno: EnvironmentConfig,
        dt: Double,
        tiempoMaximo: Double
    ): List<ProjectileState> {
        require(dt > 0.0) { "El paso temporal dt debe ser estrictamente positivo (recibido: $dt)." }
        require(tiempoMaximo > 0.0) { "El tiempo máximo de simulación debe ser positivo (recibido: $tiempoMaximo)." }

        val trayectoria = mutableListOf<ProjectileState>()
        var estadoActual = estadoInicial
        trayectoria.add(estadoActual)

        while (!estadoActual.estaEnReposo && estadoActual.tiempo < tiempoMaximo) {
            val siguienteEstado = actualizarPaso(estadoActual, proyectil, entorno, dt)
            trayectoria.add(siguienteEstado)
            estadoActual = siguienteEstado
        }

        return trayectoria.toList()
    }
}
