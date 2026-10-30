package fisicas.motor

import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.sqrt

/**
 * Objeto de valor (Value Object) inmutable que encapsula y calcula internamente
 * las métricas cinemáticas de impacto del proyectil al llegar al suelo.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es representar el estado
 *   de velocidad de impacto y derivar matemáticamente la rapidez escalar final y el ángulo de llegada.
 * - Inmutabilidad y Encapsulamiento (POO): Todas sus propiedades son de solo lectura (`val`) y las
 *   fórmulas trigonométricas se resuelven internamente sin exponer estado mutable.
 *
 * Fórmulas implementadas:
 * - Velocidad escalar final: v_final = sqrt(vx^2 + vy^2)
 * - Ángulo de llegada al suelo: theta = arctan(|vy| / vx)
 *
 * @property vx Componente horizontal de la velocidad al impactar el suelo (m/s).
 * @property vy Componente vertical de la velocidad al impactar el suelo (m/s).
 */
data class MetricasImpacto(
    val vx: Double,
    val vy: Double
) {
    /**
     * Velocidad escalar final (rapidez de impacto) en m/s: sqrt(vx^2 + vy^2).
     */
    val velocidadEscalarFinal: Double = calcularVelocidadEscalar()

    /**
     * Ángulo de llegada al suelo en radianes: arctan(|vy| / vx).
     */
    val anguloLlegadaRadianes: Double = calcularAnguloLlegadaRadianes()

    /**
     * Ángulo de llegada al suelo expresado en grados sexagesimales.
     */
    val anguloLlegadaGrados: Double = calcularAnguloLlegadaGrados()

    /**
     * Alias de conveniencia para el ángulo de llegada al suelo en grados.
     */
    val anguloLlegada: Double get() = anguloLlegadaGrados

    /**
     * Calcula internamente la velocidad escalar final como la raíz cuadrada
     * de la suma de los cuadrados de [vx] y [vy]: sqrt(vx^2 + vy^2).
     *
     * @return Magnitud escalar de la velocidad final en m/s.
     */
    fun calcularVelocidadEscalar(): Double {
        return sqrt(vx * vx + vy * vy)
    }

    /**
     * Calcula internamente el ángulo de llegada al suelo en radianes mediante
     * el arco tangente de |vy| / vx: atan(|vy| / vx).
     *
     * Si ambas componentes son cero (reposo en el origen), retorna 0.0.
     *
     * @return Ángulo de impacto en radianes.
     */
    fun calcularAnguloLlegadaRadianes(): Double {
        if (vx == 0.0 && vy == 0.0) {
            return 0.0
        }
        return atan(abs(vy) / vx)
    }

    /**
     * Calcula el ángulo de llegada al suelo convertido a grados sexagesimales.
     *
     * @return Ángulo de impacto en grados.
     */
    fun calcularAnguloLlegadaGrados(): Double {
        return Math.toDegrees(calcularAnguloLlegadaRadianes())
    }

    /**
     * Indica si el proyectil llegó al suelo desplazándose hacia atrás (vx < 0).
     */
    fun tieneRetrocesoHorizontal(): Boolean = vx < 0.0

    companion object {
        val IMPACTO_NULO = MetricasImpacto(0.0, 0.0)

        /**
         * Construye las métricas de impacto a partir de un [PuntoTrayectoria].
         */
        fun desdePunto(punto: PuntoTrayectoria): MetricasImpacto {
            return MetricasImpacto(vx = punto.vx, vy = punto.vy)
        }
    }
}
