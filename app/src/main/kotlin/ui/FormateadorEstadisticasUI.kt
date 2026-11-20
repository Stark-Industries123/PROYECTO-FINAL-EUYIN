package ui

import config.ConfigSimulacion
import fisicas.motor.ResultadoSimulacion

/**
 * Formatea los textos de resumen en tiempo real (HUD) y el reporte completo de estadísticas
 * post-partida para la interfaz gráfica 2D.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Centraliza el formateo textual de métricas físicas
 *   para la UI, desacoplándolo de la ventana y del controlador.
 */
class FormateadorEstadisticasUI(
    private val separadorDecimal: Char = '.'
) {

    /**
     * Construye la línea compacta de métricas mostrada en la barra superior del escenario de juego.
     */
    fun formatearResumenHud(config: ConfigSimulacion, resultado: ResultadoSimulacion): String {
        return String.format(
            "Planeta: %s | Tiempo: %.2f s | Alcance: %.2f m | Altura máx: %.2f m | Rebotes: %d",
            config.fuenteGravedad.obtenerNombre(),
            resultado.tiempoVuelo,
            resultado.distanciaMaxima,
            resultado.alturaMaxima,
            resultado.cantidadRebotes
        ).replace(',', separadorDecimal)
    }

    /**
     * Construye el reporte detallado de resultados finales a partir de [config] y [resultado].
     */
    fun formatearEstadisticasFinales(config: ConfigSimulacion, resultado: ResultadoSimulacion): String {
        val sector = config.sectorViento.descripcion
        return listOf(
            "Planeta objetivo: ${config.fuenteGravedad.obtenerNombre()}",
            "Gravedad aplicada: ${config.fuenteGravedad.gravedad} m/s²",
            String.format("Masa del proyectil: %.2f kg", config.masaKg).replace(',', separadorDecimal),
            String.format(
                "Viento: %.1f km/h (Sector: %s, %.1f°)",
                config.velocidadVientoKmH,
                sector,
                config.direccionVientoGrados
            ).replace(',', separadorDecimal),
            String.format(
                "Origen del viento: Sopla DESDE el %s (%.1f°)",
                sector,
                config.direccionVientoGrados
            ).replace(',', separadorDecimal),
            String.format("Tiempo de vuelo total: %.2f s", resultado.tiempoVuelo).replace(',', separadorDecimal),
            String.format("Alcance máximo frontal (X): %.2f m", resultado.distanciaMaxima).replace(',', separadorDecimal),
            "Desviación lateral (Z): 0.00 m",
            String.format("Altura máxima (Y): %.2f m", resultado.alturaMaxima).replace(',', separadorDecimal),
            "Cantidad de rebotes: ${resultado.cantidadRebotes}"
        ).joinToString("\n")
    }
}
