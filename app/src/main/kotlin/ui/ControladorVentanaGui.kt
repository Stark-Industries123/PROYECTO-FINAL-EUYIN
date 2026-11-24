package ui

import config.ConfigSimulacion
import fisicas.motor.PuntoTrayectoria
import fisicas.motor.ResultadoSimulacion
import javax.swing.JPanel

/**
 * Contrato de fachada expuesto hacia [VentanaSimulacion] para operar la interfaz gráfica
 * sin acoplar el marco Swing a controladores, formularios o motores físicos concretos (DIP).
 */
interface ControladorVentanaGui : AccionesJuego {

    /**
     * Retorna el contenedor raíz de pantallas a montar dentro de la ventana principal.
     */
    fun obtenerContenedor(): JPanel

    /**
     * Actualiza los campos del formulario de configuración y opcionalmente la dirección del viento.
     */
    fun actualizarEntradas(valores: Map<String, String>, direccionVientoGrados: Double? = null)

    /**
     * Retorna el mensaje de resumen o error actualmente visible en el HUD de juego.
     */
    fun obtenerTextoResumen(): String

    /**
     * Retorna los puntos de trayectoria cargados en el panel de juego.
     */
    fun obtenerPuntosGraficados(): List<PuntoTrayectoria>

    /**
     * Valida la configuración cargada y ejecuta la simulación física.
     */
    fun ejecutarSimulacion(): Boolean

    /**
     * Construye el texto de estadísticas finales para [config] y [resultado].
     */
    fun construirTextoEstadisticas(config: ConfigSimulacion, resultado: ResultadoSimulacion): String
}
