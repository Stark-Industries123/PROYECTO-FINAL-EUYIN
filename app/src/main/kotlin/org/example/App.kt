package org.example

import fisicas.motor.Simulador
import ui.MenuConsola

fun main() {
    val menu = MenuConsola()
    val config = menu.solicitarConfiguracion()

    val simulador = Simulador(config.fuenteGravedad)
    val resultados = simulador.calcularTrayectoria(config.velocidadInicial, config.angulo)

    println("\n--- RESULTADOS DEL IMPACTO ---")
    println("Planeta objetivo: ${config.fuenteGravedad.obtenerNombre()}")
    println("Gravedad aplicada: ${config.fuenteGravedad.gravedad} m/s²")
    println("Tiempo de vuelo: %.2f segundos".format(resultados.tiempoVuelo))
    println("Alcance máximo: %.2f metros".format(resultados.distanciaMaxima))
    println("Altura máxima: %.2f metros".format(resultados.alturaMaxima))
}
