package fisicas.motor

import kotlin.math.abs
import kotlin.math.max

/**
 * Implementación tridimensional de [SesionCinematica] que coordina la evolución de un
 * [EstadoCinematica] y acumula los puntos muestreados, alturas por pique y rebotes.
 *
 * Cumplimiento de SOLID:
 * - Single Responsibility Principle (SRP): Concentra la recolección de trayectoria y métricas
 *   de impacto durante una corrida de integración numérica.
 */
class SesionCinematica3D(
    private val estado: EstadoCinematica
) : SesionCinematica {

    private val trayectoria = mutableListOf(estado.crearPunto(0.0))
    private val piques = mutableListOf<Double>()
    private var alturaMaximaGlobal: Double = 0.0
    private var alturaMaximaPique: Double = 0.0
    private var cantidadRebotes: Int = 0

    override fun avanzarSubPaso(
        gravedad: Double,
        factorArrastre: Double,
        vientoX: Double,
        vientoZ: Double
    ): Boolean {
        val altura = estado.avanzarSubPaso(gravedad, factorArrastre, vientoX, vientoZ)
        alturaMaximaGlobal = max(alturaMaximaGlobal, altura)
        alturaMaximaPique = max(alturaMaximaPique, altura)
        val contacto = estado.evaluarContactoSuelo()
        if (contacto == 0) return false
        if (alturaMaximaPique > 1e-4) piques.add(alturaMaximaPique)
        alturaMaximaPique = 0.0
        if (contacto == 1) cantidadRebotes += 1
        return contacto == -1
    }

    override fun capturarMuestra(tiempo: Double) {
        trayectoria.add(estado.crearPunto(tiempo))
    }

    override fun construirResultado(): ResultadoSimulacion {
        if (piques.isEmpty() && alturaMaximaGlobal > 0.0) piques.add(alturaMaximaGlobal)
        val alcanceMaximo = trayectoria.maxByOrNull { abs(it.x) }?.x ?: 0.0
        return ResultadoSimulacion(
            tiempoVuelo = trayectoria.last().tiempo,
            distanciaMaxima = alcanceMaximo,
            alturaMaxima = alturaMaximaGlobal,
            trayectoria = trayectoria.toList(),
            cantidadRebotes = cantidadRebotes,
            desviacionLateralMaxima = trayectoria.maxOf { abs(it.z) },
            alturasMaximasPorPique = piques.toList()
        )
    }
}
