package fisicas.motor

import kotlin.math.abs
import kotlin.math.max

/**
 * Implementación concreta de [RegistroTrayectoria] que acumula la serie temporal de puntos,
 * las alturas máximas por pique y el conteo de rebotes para construir [ResultadoSimulacion].
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Responsable únicamente de acumular muestras y
 *   derivar las métricas finales de la simulación sin acoplarse a la física diferencial.
 * - Encapsulamiento: Todo el estado acumulado permanece privado y solo se expone a través
 *   del objeto inmutable [ResultadoSimulacion].
 */
class RegistroTrayectoria3D : RegistroTrayectoria {

    private val trayectoria: MutableList<PuntoTrayectoria> = mutableListOf()
    private val piques: MutableList<Double> = mutableListOf()
    private var alturaMaximaGlobal: Double = 0.0
    private var alturaMaximaPiqueActual: Double = 0.0
    private var cantidadRebotes: Int = 0

    override fun registrarSubPaso(alturaActual: Double, codigoContacto: Int) {
        alturaMaximaGlobal = max(alturaMaximaGlobal, alturaActual)
        alturaMaximaPiqueActual = max(alturaMaximaPiqueActual, alturaActual)
        if (codigoContacto == 0) return
        if (alturaMaximaPiqueActual > UMBRAL_PIQUE) {
            piques.add(alturaMaximaPiqueActual)
        }
        alturaMaximaPiqueActual = 0.0
        if (codigoContacto == 1) {
            cantidadRebotes += 1
        }
    }

    override fun registrarPunto(punto: PuntoTrayectoria) {
        trayectoria.add(punto)
    }

    override fun construirResultado(): ResultadoSimulacion {
        val alturasPiques = if (piques.isEmpty() && alturaMaximaGlobal > 0.0) {
            listOf(alturaMaximaGlobal)
        } else {
            piques.toList()
        }
        val tiempoVuelo = trayectoria.lastOrNull()?.tiempo ?: 0.0
        val alcanceMaximo = trayectoria.maxByOrNull { abs(it.x) }?.x ?: 0.0
        val desviacionLateral = trayectoria.maxOfOrNull { abs(it.z) } ?: 0.0
        return ResultadoSimulacion(
            tiempoVuelo = tiempoVuelo,
            distanciaMaxima = alcanceMaximo,
            alturaMaxima = alturaMaximaGlobal,
            trayectoria = trayectoria.toList(),
            cantidadRebotes = cantidadRebotes,
            desviacionLateralMaxima = desviacionLateral,
            alturasMaximasPorPique = alturasPiques
        )
    }

    companion object {
        private const val UMBRAL_PIQUE: Double = 1e-4
    }
}
