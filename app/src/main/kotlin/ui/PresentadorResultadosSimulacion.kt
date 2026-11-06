package ui

import condiciones.io.EscritorConsola
import condiciones.io.EscritorConsolaEstandar
import config.ConfigSimulacion
import fisicas.motor.ResultadoSimulacion

/**
 * Servicio de presentación por consola encargado exclusivamente de formatear y emitir
 * el reporte de resultados de impacto y delegar la visualización gráfica de la trayectoria.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es presentar los
 *   resultados de una simulación finalizada. No solicita entradas ni calcula física.
 */
class PresentadorResultadosSimulacion(
    private val escritor: EscritorConsola = EscritorConsolaEstandar(),
    private val graficador: Graficador = GraficadorConsola(escritorLinea = { escritor.escribirLinea(it) })
) : PresentadorResultados {

    /**
     * Emite el reporte numérico de impacto seguido de la gráfica ASCII de la trayectoria.
     */
    override fun presentarCompleto(config: ConfigSimulacion, resultados: ResultadoSimulacion) {
        escritor.escribirLinea("\n--- RESULTADOS DEL IMPACTO ---")
        mostrarDatosEntorno(config)
        mostrarDatosViento(config)
        mostrarMetricasTrayectoria(resultados)
        graficador.graficar(resultados.trayectoria)
    }

    /**
     * Muestra el reporte final del impacto utilizando los valores dinámicos configurados.
     */
    override fun mostrarResultados(config: ConfigSimulacion, resultados: ResultadoSimulacion) {
        escritor.escribirLinea("\n--- RESULTADOS DEL IMPACTO ---")
        mostrarDatosEntorno(config)
        mostrarDatosViento(config)
        mostrarMetricasTrayectoria(resultados)
    }

    private fun mostrarDatosEntorno(config: ConfigSimulacion) {
        escritor.escribirLinea("Planeta objetivo: ${config.fuenteGravedad.obtenerNombre()}")
        escritor.escribirLinea("Gravedad aplicada: ${config.fuenteGravedad.gravedad} m/s²")
        escritor.escribirLinea("Masa del proyectil: %.2f kg".format(config.masaKg).replace(',', '.'))
    }

    private fun mostrarDatosViento(config: ConfigSimulacion) {
        val viento = config.viento
        if (viento != null) {
            val rumbo = viento.rumbo
            val detalle = "Viento: %.1f km/h (Sector: %s, Rumbo: %s - %s, %.1f°)".format(
                config.velocidadVientoKmH, config.sectorViento.descripcion,
                rumbo.codigo, rumbo.nombreCompleto, config.direccionVientoGrados
            ).replace(',', '.')
            escritor.escribirLinea(detalle)
            escritor.escribirLinea("Origen del viento: Sopla DESDE el rumbo ${rumbo.codigo} (${rumbo.nombreCompleto})")
        } else {
            val resumen = "Viento: %.1f km/h (Sector: %s, %.1f°)".format(
                config.velocidadVientoKmH, config.sectorViento.descripcion, config.direccionVientoGrados
            ).replace(',', '.')
            escritor.escribirLinea(resumen)
        }
    }

    private fun mostrarMetricasTrayectoria(resultados: ResultadoSimulacion) {
        escritor.escribirLinea("Tiempo de vuelo total: %.2f segundos".format(resultados.tiempoVuelo).replace(',', '.'))
        escritor.escribirLinea("Alcance máximo frontal (X): %.2f metros".format(resultados.distanciaMaxima).replace(',', '.'))
        escritor.escribirLinea("Desviación lateral (Z): %.2f metros".format(resultados.desviacionLateralMaxima).replace(',', '.'))
        escritor.escribirLinea("Altura máxima (Y): %.2f metros".format(resultados.alturaMaxima).replace(',', '.'))
        escritor.escribirLinea("Cantidad de rebotes: ${resultados.cantidadRebotes}")
    }
}
