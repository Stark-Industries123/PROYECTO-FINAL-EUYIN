package ui

import config.ConfigSimulacion
import fisicas.motor.ResultadoSimulacion

/**
 * Contrato de abstracción para la presentación de resultados de la simulación.
 */
interface PresentadorResultados {

    /**
     * Emite el reporte numérico de impacto seguido de la visualización gráfica de la trayectoria.
     */
    fun presentarCompleto(config: ConfigSimulacion, resultados: ResultadoSimulacion)

    /**
     * Muestra el reporte numérico final del impacto.
     */
    fun mostrarResultados(config: ConfigSimulacion, resultados: ResultadoSimulacion)
}
