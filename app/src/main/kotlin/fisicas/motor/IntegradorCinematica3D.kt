package fisicas.motor

import config.ConfigSimulacion
import kotlin.math.round

/**
 * Servicio de integración numérica tridimensional encargado de coordinar el bucle
 * temporal de muestreo y sub-integración para construir [ResultadoSimulacion].
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es dirigir el avance
 *   temporal por ventanas de muestreo y sub-pasos delegando la evolución cinemática y
 *   el registro de métricas a la abstracción [EstadoCinematica].
 * - Dependency Inversion Principle (DIP): Depende exclusivamente de la abstracción
 *   [FabricaEstadoCinematica] y del objeto de configuración cohesivo [ParametrosIntegracion],
 *   sin referencias a clases concretas de estado o registro.
 * - Bajo Acoplamiento (CBO): Reduce sus dependencias externas al operar sobre un único
 *   objeto de configuración y contratos abstractos.
 */
class IntegradorCinematica3D(
    private val parametros: ParametrosIntegracion,
    private val fabricaEstado: FabricaEstadoCinematica
) : IntegradorCinematica {

    /**
     * Ejecuta la integración numérica completa para la configuración dada.
     */
    override fun simular(config: ConfigSimulacion): ResultadoSimulacion {
        val estado = fabricaEstado.crear(config, parametros)
        val subPasos = parametros.calcularSubPasos()
        estado.registrarMuestra(0.0)
        var tiempoActual = 0.0
        var enReposo = false

        while (!enReposo && tiempoActual < parametros.tiempoMaximo) {
            enReposo = ejecutarVentanaMuestreo(estado, subPasos)
            tiempoActual += parametros.pasoTiempoMuestreo
            estado.registrarMuestra(round(tiempoActual * 1000.0) / 1000.0)
        }

        return estado.construirResultado()
    }

    private fun ejecutarVentanaMuestreo(estado: EstadoCinematica, subPasos: Int): Boolean {
        for (paso in 0 until subPasos) {
            if (estado.avanzarSubPaso()) return true
        }
        return false
    }
}
