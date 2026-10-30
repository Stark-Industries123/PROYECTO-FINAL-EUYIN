package fisicas.vector

import kotlin.math.hypot

/**
 * Representa un vector bidimensional inmutable con operaciones algebraicas y geométricas.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Encapsula exclusivamente las propiedades matemáticas y operaciones
 *   asociadas a vectores en un espacio Euclídeo bidimensional (2D).
 * - Inmutabilidad (POO): Todas las operaciones generan una nueva instancia sin alterar el estado previo,
 *   garantizando thread-safety y ausencia de efectos colaterales.
 * - Evita clases anémicas: Contiene comportamiento y sobrecarga de operadores matemáticos idiomáticos de Kotlin.
 *
 * @property x Componente horizontal del vector.
 * @property y Componente vertical del vector.
 */
data class Vector2D(
    val x: Double,
    val y: Double
) {

    /**
     * Suma vectorial término a término: (x1 + x2, y1 + y2).
     */
    operator fun plus(otro: Vector2D): Vector2D = Vector2D(x + otro.x, y + otro.y)

    /**
     * Resta vectorial término a término: (x1 - x2, y1 - y2).
     */
    operator fun minus(otro: Vector2D): Vector2D = Vector2D(x - otro.x, y - otro.y)

    /**
     * Multiplicación por un escalar: (x * k, y * k).
     */
    operator fun times(escalar: Double): Vector2D = Vector2D(x * escalar, y * escalar)

    /**
     * División por un escalar: (x / k, y / k).
     * @throws IllegalArgumentException si el escalar es cero.
     */
    operator fun div(escalar: Double): Vector2D {
        require(escalar != 0.0) { "No se puede dividir un vector por un escalar cero." }
        return Vector2D(x / escalar, y / escalar)
    }

    /**
     * Negación vectorial (vector opuesto): (-x, -y).
     */
    operator fun unaryMinus(): Vector2D = Vector2D(-x, -y)

    /**
     * Retorna la norma euclídea o magnitud del vector: sqrt(x^2 + y^2).
     */
    fun magnitud(): Double = hypot(x, y)

    /**
     * Retorna el cuadrado de la magnitud (x^2 + y^2), útil para optimizar comparaciones sin raíz cuadrada.
     */
    fun magnitudCuadrada(): Double = x * x + y * y

    /**
     * Retorna el vector unitario en la misma dirección.
     * Si la magnitud es 0, retorna el vector [CERO].
     */
    fun normalizar(): Vector2D {
        val mag = magnitud()
        return if (mag > EPSILON) this / mag else CERO
    }

    /**
     * Calcula el producto escalar o producto punto con otro vector: (x1 * x2 + y1 * y2).
     */
    fun productoPunto(otro: Vector2D): Double = x * otro.x + y * otro.y

    /**
     * Calcula la distancia euclidiana entre este vector y otro punto/vector.
     */
    fun distanciaA(otro: Vector2D): Double = (this - otro).magnitud()

    companion object {
        private const val EPSILON: Double = 1e-12

        val CERO = Vector2D(0.0, 0.0)
        val UNITARIO_X = Vector2D(1.0, 0.0)
        val UNITARIO_Y = Vector2D(0.0, 1.0)
    }
}
