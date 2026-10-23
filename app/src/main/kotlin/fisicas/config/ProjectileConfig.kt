package fisicas.config

/**
 * Modela las propiedades físicas intrínsecas del proyectil (masa y resistencia del aire).
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Encapsula exclusivamente las propiedades inherentes al objeto lanzado.
 * - Inmutabilidad (POO): Estructura inmutable con validación de invariantes físicas en construcción.
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

    companion object {
        const val DEFAULT_MASA_KG: Double = 1.0
        const val DEFAULT_COEFICIENTE_ARRASTRE: Double = 0.0
    }
}
