package fisicas.motor

import config.ConfigSimulacion

/**
 * Doble de prueba para [FabricaEstadoCinematica] que permite verificar el desacoplamiento
 * de [IntegradorCinematica3D] respecto de implementaciones concretas de [EstadoCinematica].
 */
class FabricaEstadoCinematicaFake(
    private val subPasosHastaReposo: Int
) : FabricaEstadoCinematica {

    private var llamadasCrear: Int = 0
    private var subPasosEjecutados: Int = 0
    private val tiemposMuestreados = mutableListOf<Double>()

    override fun crear(config: ConfigSimulacion, parametros: ParametrosIntegracion): EstadoCinematica {
        llamadasCrear += 1
        return object : EstadoCinematica {
            override fun avanzarSubPaso(): Boolean {
                subPasosEjecutados += 1
                return subPasosEjecutados >= subPasosHastaReposo
            }

            override fun registrarMuestra(tiempo: Double) {
                tiemposMuestreados.add(tiempo)
            }

            override fun construirResultado(): ResultadoSimulacion {
                return ResultadoSimulacion(
                    tiempoVuelo = tiemposMuestreados.lastOrNull() ?: 0.0,
                    distanciaMaxima = 0.0,
                    alturaMaxima = 0.0
                )
            }
        }
    }

    fun obtenerLlamadasCrear(): Int = llamadasCrear

    fun obtenerSubPasosEjecutados(): Int = subPasosEjecutados

    fun obtenerTiemposMuestreados(): List<Double> = tiemposMuestreados.toList()
}
