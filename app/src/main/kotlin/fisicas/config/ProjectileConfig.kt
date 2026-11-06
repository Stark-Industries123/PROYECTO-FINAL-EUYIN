package fisicas.config

import fisicas.vector.Vector2D

/**
 * Modela las propiedades físicas intrínsecas del proyectil (masa y resistencia del aire).
 *
 * @property masa Masa del proyectil en kilogramos (debe ser estrictamente > 0.0).
 * @property coeficienteArrastre Coeficiente k de arrastre o resistencia del aire F_drag = -k * v (debe ser >= 0.0).
 */
data class ProjectileConfig(
    val masa: Double,
    val coeficienteArrastre: Double = DEFAULT_COEFICIENTE_ARRASTRE
) {
    init {
        require(masa > 0.0) {
            "La masa del proyectil debe ser estrictamente mayor a 0 kg (recibido: $masa)."
        }
        require(coeficienteArrastre >= 0.0) {
            "El coeficiente de arrastre debe ser mayor o igual a 0.0 (recibido: $coeficienteArrastre)."
        }
    }

    /**
     * Calcula la fuerza de resistencia aerodinámica lineal opuesta al vector velocidad.
     */
    fun calcularFuerzaArrastre(velocidad: Vector2D): Vector2D = velocidad * (-coeficienteArrastre)

    companion object {
        const val DEFAULT_MASA_KG: Double = 1.0
        const val DEFAULT_COEFICIENTE_ARRASTRE: Double = 0.0
    }
}
