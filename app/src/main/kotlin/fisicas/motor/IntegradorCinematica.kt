package fisicas.motor

import config.ConfigSimulacion

/**
 * Contrato de abstracción para el servicio de integración numérica tridimensional de trayectorias.
 */
interface IntegradorCinematica {

    /**
     * Ejecuta la integración numérica completa para la configuración dada.
     */
    fun simular(config: ConfigSimulacion): ResultadoSimulacion
}
