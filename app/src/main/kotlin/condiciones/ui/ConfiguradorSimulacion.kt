package condiciones.ui

import config.ConfigSimulacion

/**
 * Contrato de abstracción para el configurador interactivo de simulación.
 */
interface ConfiguradorSimulacion {

    /**
     * Solicita los parámetros de disparo y retorna un [ConfigSimulacion] inicializado.
     */
    fun solicitarConfiguracion(): ConfigSimulacion
}
