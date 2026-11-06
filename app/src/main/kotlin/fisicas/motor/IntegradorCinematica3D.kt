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
    private val coeficienteRestitucion: Double = 0.75,
    private val coeficienteArrastre: Double = 0.05,
    private val pasoTiempoMuestreo: Double = 0.1,
    private val proveedorEstado: (Double, Double, Double, Double) -> EstadoCinematica = { v0, ang, dt, rest ->
        EstadoCinematica3D(v0, ang, dt, rest)
    }
) : IntegradorCinematica {

    init {
        require(coeficienteRestitucion in 0.0..1.0) {
            "El coeficiente de restitución debe estar entre 0.0 y 1.0 (recibido: $coeficienteRestitucion)."
        }
        require(coeficienteArrastre >= 0.0) {
            "El coeficiente de arrastre aerodinámico debe ser mayor o igual a 0 (recibido: $coeficienteArrastre)."
        }
        require(pasoTiempoMuestreo > 0.0) {
            "El paso de tiempo de muestreo debe ser estrictamente mayor a 0 (recibido: $pasoTiempoMuestreo)."
        }
    }

    /**
     * Ejecuta la integración numérica completa para la configuración dada.
     */
    override fun simular(config: ConfigSimulacion): ResultadoSimulacion {
        val subPasos = max(1, (pasoTiempoMuestreo / 0.001).toInt())
        val dt = pasoTiempoMuestreo / subPasos
        val estado = proveedorEstado(config.velocidadInicial, config.angulo, dt, coeficienteRestitucion)
        val trayectoria = mutableListOf(estado.crearPunto(0.0))
        val piques = mutableListOf<Double>()
        val metricas = ejecutarSimulacion(config, estado, trayectoria, piques)
        return ResultadoSimulacion(
            tiempoVuelo = trayectoria.last().tiempo,
            distanciaMaxima = calcularAlcanceMaximo(trayectoria),
            alturaMaxima = metricas[0],
            trayectoria = trayectoria,
            cantidadRebotes = metricas[2].toInt(),
            desviacionLateralMaxima = trayectoria.maxOf { abs(it.z) },
            alturasMaximasPorPique = piques
        )
    }

    private fun calcularAlcanceMaximo(trayectoria: List<PuntoTrayectoria>): Double {
        return trayectoria.maxByOrNull { abs(it.x) }?.x ?: 0.0
    }

    private fun ejecutarSimulacion(
        config: ConfigSimulacion,
        estado: EstadoCinematica,
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
        estado: EstadoCinematica,
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
