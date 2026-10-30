package condiciones.dominio

import kotlin.math.cos
import kotlin.math.sin

/**
 * Modelo de dominio que representa las condiciones del viento como perturbación atmosférica
 * sobre la trayectoria parabólica.
 *
 * Convención Náutica y Meteorológica:
 * - La dirección indica el rumbo DESDE DONDE SOPLA el viento (origen del flujo).
 * - 0° representa el Norte, 90° el Este, 180° el Sur y 270° el Oeste.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Modela exclusivamente los parámetros atmosféricos
 *   del viento, su asociación con el rumbo náutico de la rosa de los vientos y su proyección vectorial.
 * - Inmutabilidad (POO): Estructura libre de efectos colaterales con validación previa de estado.
 * - Cohesión: Encapsula el cálculo de componentes vectoriales cartesianas y conversiones de unidades.
 *
 * @property direccionGrados Dirección azimutal desde donde sopla el viento en grados (0.0 a 360.0).
 * @property velocidad Velocidad o magnitud del viento (km/h, debe ser >= 0.0).
 * @property rumbo Rumbo náutico exacto asignado matemáticamente según los 32 puntos ([PuntoRosaVientos]).
 */
data class Viento(
    val direccionGrados: Double,
    val velocidad: Double,
    val rumbo: PuntoRosaVientos
) {
    init {
        require(direccionGrados in 0.0..360.0) {
            "La dirección del viento debe estar entre 0.0 y 360.0 grados (recibido: $direccionGrados)."
        }
        require(velocidad >= 0.0) {
            "La velocidad del viento debe ser mayor o igual a 0.0 (recibido: $velocidad)."
        }
    }

    /**
     * Determina si el viento se encuentra en condición de calma (velocidad igual a 0).
     */
    fun esCalma(): Boolean = velocidad == 0.0

    /**
     * Retorna la velocidad del viento convertida a metros por segundo (m/s).
     */
    fun velocidadEnMS(): Double = velocidad / FACTOR_KMH_A_MS

    /**
     * Calcula la proyección vectorial del viento en el plano horizontal bidimensional (X: Este, Y: Norte).
     *
     * @return Par con las componentes `(Vx, Vy)` en las mismas unidades de [velocidad].
     */
    fun componentesVectoriales(): Pair<Double, Double> {
        val rad = Math.toRadians(direccionGrados)
        val vx = velocidad * sin(rad)
        val vy = velocidad * cos(rad)
        return Pair(vx, vy)
    }

    /**
     * Genera una descripción textual legible del estado del viento.
     */
    fun descripcionFormateada(): String {
        return if (esCalma()) {
            "Viento en calma (0.00 km/h)"
        } else {
            "Viento de %.2f km/h soplando desde el rumbo %s (%s, %.2f°) [azimutal: %.2f°]".format(
                velocidad,
                rumbo.codigo,
                rumbo.nombreCompleto,
                rumbo.anguloNominal,
                direccionGrados
            )
        }
    }

    companion object {
        const val FACTOR_KMH_A_MS: Double = 3.6
    }
}
