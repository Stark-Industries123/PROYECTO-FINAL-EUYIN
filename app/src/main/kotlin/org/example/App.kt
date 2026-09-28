package org.example

import fisicas.gravedad.*
import fisicas.motor.Simulador

fun main() {
    println("=== SIMULADOR DE TIRO PARABÓLICO ===")
    
    val planeta = seleccionarPlaneta()
    val velocidad = solicitarValorDoble("Introduce la velocidad inicial (m/s): ")
    val angulo = solicitarValorDoble("Introduce el ángulo de disparo (grados, 0-90): ")

    if (angulo < 0 || angulo > 90) {
        println("Error: El ángulo debe estar entre 0 y 90 grados.")
        return
    }

    val simulador = Simulador(planeta)
    val resultados = simulador.calcularTrayectoria(velocidad, angulo)

    println("\n--- RESULTADOS DEL IMPACTO ---")
    println("Planeta objetivo: ${planeta.obtenerNombre()}")
    println("Gravedad aplicada: ${planeta.gravedad} m/s²")
    println("Tiempo total de vuelo: %.2f segundos".format(resultados.tiempoVuelo))
    println("Distancia máxima (Alcance): %.2f metros".format(resultados.distanciaMaxima))
    println("Altura máxima alcanzada: %.2f metros".format(resultados.alturaMaxima))
}

fun seleccionarPlaneta(): FuenteGravedad {
    while (true) {
        println("\nSelecciona el entorno de simulación:")
        println("1. Tierra")
        println("2. Luna")
        println("3. Mercurio")
        println("4. Júpiter")
        print("Opción (1-4): ")

        val entrada = readlnOrNull()?.trim() ?: continue

        when (entrada) {
            "1" -> return Tierra()
            "2" -> return Luna()
            "3" -> return Mercurio()
            "4" -> return Jupiter()
            else -> println("Opción inválida. Por favor, selecciona un número del 1 al 4.")
        }
    }
}

fun solicitarValorDoble(mensaje: String): Double {
    while (true) {
        print(mensaje)
        val entrada = readlnOrNull()?.trim() ?: continue
        
        try {
            val valor = entrada.toDouble()
            if (valor >= 0) return valor
            println("Por favor, ingresa un número positivo válido.")
        } catch (e: NumberFormatException) {
            println("Entrada inválida. Asegúrate de ingresar un número válido (ej: 15.5).")
        }
    }
}