package fisicas.config

import fisicas.vector.Vector2D

/**
 * Encapsula los parámetros físicos para la simulación del impacto y rebote contra el suelo (Y <= 0).
 *
 * @property coeficienteRestitucion Factor e de conservación de velocidad vertical post-impacto (0.0 a 1.0).
 * @property friccionSuelo Factor de conservación de velocidad horizontal post-impacto (0.0 a 1.0).
 * @property umbralReposo Umbral de rapidez total por debajo del cual el proyectil pasa al reposo absoluto (m/s).
 */
data class BounceConfig(
    val coeficienteRestitucion: Double = DEFAULT_COEFICIENTE_RESTITUCION,
    val friccionSuelo: Double = DEFAULT_FRICCION_SUELO,
    val umbralReposo: Double = DEFAULT_UMBRAL_REPOSO
) {
    init {
        require(coeficienteRestitucion in 0.0..1.0) {
            "El coeficiente de restitución debe estar comprendido entre 0.0 y 1.0 (recibido: $coeficienteRestitucion)."
        }
        require(friccionSuelo in 0.0..1.0) {
            "El factor de fricción del suelo debe estar comprendido entre 0.0 y 1.0 (recibido: $friccionSuelo)."
        }
        require(umbralReposo >= 0.0) {
            "El umbral de reposo debe ser mayor o igual a 0.0 (recibido: $umbralReposo)."
        }
    }

    /**
     * Calcula el nuevo vector de velocidad aplicando restitución vertical y fricción horizontal.
     */
    fun calcularVelocidadRebote(velocidad: Vector2D): Vector2D = Vector2D(velocidad.x * friccionSuelo, -coeficienteRestitucion * velocidad.y)

    /**
     * Evalúa si una velocidad post-impacto está por debajo del umbral de reposo.
     */
    fun debeDetenerse(velocidad: Vector2D): Boolean = velocidad.magnitud() < umbralReposo && coeficienteRestitucion >= 0.0

    companion object {
        const val DEFAULT_COEFICIENTE_RESTITUCION: Double = 0.75
        const val DEFAULT_FRICCION_SUELO: Double = 1.0
        const val DEFAULT_UMBRAL_REPOSO: Double = 0.05
    }
}
