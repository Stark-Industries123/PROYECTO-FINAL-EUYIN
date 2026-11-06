package fisicas.motor

import config.ConfigSimulacion
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.round
import kotlin.math.sin

/**
 * Servicio de integración numérica tridimensional encargado de evolucionar el movimiento
 * del proyectil en el tiempo y recolectar la serie muestreada de puntos y piques.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es coordinar el bucle
 *   temporal de muestreo y sub-integración para construir [ResultadoSimulacion].
 */
class IntegradorCinematica3D(
    private val coeficienteRestitucion: Double,
    private val coeficienteArrastre: Double,
    private val pasoTiempoMuestreo: Double
) {

    /**
     * Ejecuta la integración numérica completa para la configuración dada.
     */
    fun simular(config: ConfigSimulacion): ResultadoSimulacion {
        val subPasos = max(1, (pasoTiempoMuestreo / 0.001).toInt())
        val dt = pasoTiempoMuestreo / subPasos
        val estado = EstadoCinematica3D(config.velocidadInicial, config.angulo, dt, coeficienteRestitucion)
        val trayectoria = mutableListOf(estado.crearPunto(0.0))
        val piques = mutableListOf<Double>()
        val metricas = ejecutarSimulacion(config, estado, trayectoria, piques)
        return ResultadoSimulacion(
            tiempoVuelo = trayectoria.last().tiempo,
            distanciaMaxima = max(0.0, trayectoria.maxOf { it.x }),
            alturaMaxima = metricas[0],
            trayectoria = trayectoria,
            cantidadRebotes = metricas[2].toInt(),
            desviacionLateralMaxima = trayectoria.maxOf { abs(it.z) },
            alturasMaximasPorPique = piques
        )
    }

    private fun ejecutarSimulacion(
        config: ConfigSimulacion,
        estado: EstadoCinematica3D,
        trayectoria: MutableList<PuntoTrayectoria>,
        piques: MutableList<Double>
    ): DoubleArray {
        val metricas = DoubleArray(3)
        var tiempoActual = 0.0
        var enReposo = false
        while (!enReposo && tiempoActual < 300.0) {
            enReposo = ejecutarVentanaMuestreo(config, estado, piques, metricas)
            tiempoActual += pasoTiempoMuestreo
            trayectoria.add(estado.crearPunto(round(tiempoActual * 1000.0) / 1000.0))
        }
        if (piques.isEmpty() && metricas[0] > 0.0) piques.add(metricas[0])
        return metricas
    }

    private fun ejecutarVentanaMuestreo(
        config: ConfigSimulacion,
        estado: EstadoCinematica3D,
        piques: MutableList<Double>,
        metricas: DoubleArray
    ): Boolean {
        val subPasos = max(1, (pasoTiempoMuestreo / 0.001).toInt())
        val factorArrastre = coeficienteArrastre / config.masaKg
        val rad = Math.toRadians(config.direccionVientoGrados)
        val vientoX = -config.velocidadVientoMS() * cos(rad)
        val vientoZ = -config.velocidadVientoMS() * sin(rad)
        for (paso in 0 until subPasos) {
            estado.avanzarSubPaso(config.fuenteGravedad.gravedad, factorArrastre, vientoX, vientoZ)
            if (estado.registrarImpacto(piques, metricas)) return true
        }
        return false
    }
}
