package ui

import config.ConfigSimulacion
import fisicas.motor.ResultadoSimulacion
import java.awt.Component
import java.awt.GraphicsEnvironment
import javax.swing.JOptionPane

/**
 * Presentador de estadísticas post-partida y diálogos modales de error para la interfaz Swing.
 *
 * Responsabilidad única (SRP): formatear el reporte de métricas finales y mostrar los cuadros
 * de diálogo únicamente cuando el entorno gráfico está activo y visible.
 */
class DialogoEstadisticas(
    private val componentePadre: Component
) {

    /**
     * Construye el listado legible de estadísticas finales a partir de [config] y [resultado].
     */
    fun construirTextoEstadisticas(config: ConfigSimulacion, resultado: ResultadoSimulacion): String {
        val sector = config.sectorViento.descripcion
        return listOf(
            "Planeta objetivo: ${config.fuenteGravedad.obtenerNombre()}",
            "Gravedad aplicada: ${config.fuenteGravedad.gravedad} m/s²",
            String.format("Masa del proyectil: %.2f kg", config.masaKg).replace(',', '.'),
            String.format("Viento: %.1f km/h (Sector: %s, %.1f°)", config.velocidadVientoKmH, sector, config.direccionVientoGrados).replace(',', '.'),
            String.format("Origen del viento: Sopla DESDE el %s (%.1f°)", sector, config.direccionVientoGrados).replace(',', '.'),
            String.format("Tiempo de vuelo total: %.2f s", resultado.tiempoVuelo).replace(',', '.'),
            String.format("Alcance máximo frontal (X): %.2f m", resultado.distanciaMaxima).replace(',', '.'),
            "Desviación lateral (Z): 0.00 m",
            String.format("Altura máxima (Y): %.2f m", resultado.alturaMaxima).replace(',', '.'),
            "Cantidad de rebotes: ${resultado.cantidadRebotes}"
        ).joinToString("\n")
    }

    /**
     * Muestra el cuadro de diálogo modal con las estadísticas finales de la simulación.
     */
    fun mostrarEstadisticas(config: ConfigSimulacion, resultado: ResultadoSimulacion) {
        val mensaje = construirTextoEstadisticas(config, resultado)
        if (!GraphicsEnvironment.isHeadless() && componentePadre.isShowing) {
            JOptionPane.showMessageDialog(
                componentePadre,
                mensaje,
                "Estadísticas Finales de la Misión",
                JOptionPane.INFORMATION_MESSAGE
            )
        }
    }

    /**
     * Muestra un cuadro de diálogo modal informando un error de validación de entrada.
     */
    fun mostrarError(mensaje: String) {
        if (!GraphicsEnvironment.isHeadless() && componentePadre.isShowing) {
            JOptionPane.showMessageDialog(
                componentePadre,
                mensaje,
                "Error de validación",
                JOptionPane.ERROR_MESSAGE
            )
        }
    }
}
