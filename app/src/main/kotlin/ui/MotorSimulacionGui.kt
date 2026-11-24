package ui

import config.ConfigSimulacion
import fisicas.motor.ResultadoSimulacion

/**
 * Contrato de fachada de aplicación que coordina la validación del formulario, la invocación
 * al motor físico y la presentación de estadísticas de la simulación GUI.
 */
interface MotorSimulacionGui {

    /**
     * Sincroniza la selección inicial del planeta en el formulario y retorna su nombre.
     */
    fun sincronizarPlanetaInicial(): String

    /**
     * Actualiza el planeta seleccionado por índice y retorna su nombre.
     */
    fun actualizarPlanetaPorIndice(indice: Int): String

    /**
     * Actualiza los valores de texto del formulario y opcionalmente la dirección del viento.
     */
    fun actualizarEntradas(valores: Map<String, String>, direccionVientoGrados: Double? = null)

    /**
     * Valida las entradas actuales, ejecuta el motor físico y notifica mediante [alResultado] o [alError].
     */
    fun ejecutar(
        alResultado: (String, ResultadoSimulacion) -> Unit,
        alError: (String) -> Unit
    ): Boolean

    /**
     * Presenta las estadísticas de la última simulación ejecutada, si existe.
     */
    fun mostrarUltimasEstadisticas()

    /**
     * Construye el texto de estadísticas finales para [config] y [resultado].
     */
    fun construirTextoEstadisticas(config: ConfigSimulacion, resultado: ResultadoSimulacion): String
}
