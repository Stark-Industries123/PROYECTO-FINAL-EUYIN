package ui

import config.ConfigSimulacion
import fisicas.motor.ResultadoSimulacion

/**
 * Contrato para la presentación de estadísticas finales y mensajes de error en la interfaz Swing.
 */
interface PresentadorEstadisticasGui {

    /**
     * Construye el texto legible de estadísticas a partir de [config] y [resultado].
     */
    fun construirTextoEstadisticas(config: ConfigSimulacion, resultado: ResultadoSimulacion): String

    /**
     * Presenta al usuario las estadísticas finales de la simulación.
     */
    fun mostrarEstadisticas(config: ConfigSimulacion, resultado: ResultadoSimulacion)

    /**
     * Presenta al usuario un mensaje descriptivo de error de validación.
     */
    fun mostrarError(mensaje: String)
}
