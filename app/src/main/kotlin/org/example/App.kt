package org.example

import config.ConfigSimulacion
import fisicas.motor.CalculadorTrayectoria
import fisicas.motor.ResultadoSimulacion
import fisicas.motor.Simulador
import ui.Graficador
import ui.GraficadorConsola
import ui.MenuConsola

/**
 * Punto de entrada principal y orquestador de la aplicación de tiro parabólico.
 *
 * Cumplimiento de principios POO y SOLID:
 * - 100% Orientado a Objetos: Toda la lógica y el punto de entrada principal residen dentro de la clase [App].
 * - Dependency Inversion Principle (DIP): Depende de las abstracciones [CalculadorTrayectoria] y [Graficador],
 *   permitiendo inyectar colaboradores alternativos para testing o distintas interfaces.
 * - Single Responsibility Principle (SRP): Orquesta la interacción entre la interfaz de usuario,
 *   el motor de física y la visualización sin acoplarse a detalles de implementación específicos.
 */
class App(
    private val menu: MenuConsola = MENU_DEFECTO,
    private val fabricaSimulador: (ConfigSimulacion) -> CalculadorTrayectoria = FABRICA_SIMULADOR_DEFECTO,
    private val graficador: Graficador = GRAFICADOR_DEFECTO
) {

    fun ejecutar() {
        val config = menu.solicitarConfiguracion()
        val simulador = fabricaSimulador(config)
        val resultados = simulador.calcularTrayectoria(config)

        mostrarResultados(config, resultados)
        graficador.graficar(resultados.trayectoria)
    }

    private fun mostrarResultados(config: ConfigSimulacion, resultados: ResultadoSimulacion) {
        println("\n--- RESULTADOS DEL IMPACTO ---")
        println("Planeta objetivo: ${config.fuenteGravedad.obtenerNombre()}")
        println("Gravedad aplicada: ${config.fuenteGravedad.gravedad} m/s²")
        println("Masa del proyectil: %.2f kg".format(config.masaKg))
        println("Viento: %.1f km/h (Sector: %s, %.1f°)".format(
            config.velocidadVientoKmH,
            config.sectorViento.descripcion,
            config.direccionVientoGrados
        ))
        println("Tiempo de vuelo total: %.2f segundos".format(resultados.tiempoVuelo))
        println("Alcance máximo frontal (X): %.2f metros".format(resultados.distanciaMaxima))
        println("Desviación lateral (Z): %.2f metros".format(resultados.desviacionLateralMaxima))
        println("Altura máxima (Y): %.2f metros".format(resultados.alturaMaxima))
        println("Cantidad de rebotes: ${resultados.cantidadRebotes}")
    }

    companion object {
        val MENU_DEFECTO: MenuConsola = MenuConsola()
        val FABRICA_SIMULADOR_DEFECTO: (ConfigSimulacion) -> CalculadorTrayectoria = { config -> Simulador(config.fuenteGravedad) }
        val GRAFICADOR_DEFECTO: Graficador = GraficadorConsola()

        @JvmStatic
        fun main(args: Array<String> = emptyArray()) {
            App().ejecutar()
        }
    }
}
