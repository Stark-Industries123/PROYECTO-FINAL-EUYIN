package condiciones.dominio

/**
 * Representa uno de los 32 puntos (rumbos o cuartas) de la rosa de los vientos náutica.
 * Cada punto abarca exactamente 11.25° del círculo azimutal (360° / 32 = 11.25°),
 * extendiéndose ±5.625° a cada lado de su ángulo nominal.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Modela la identidad, límites angulares y geometría
 *   de un rumbo de navegación.
 * - Inmutabilidad (POO): Estructura inmutable `val` con validaciones robustas en construcción.
 * - Alta Cohesión: Provee operaciones de pertenencia angular y cálculo de desviación respecto al centro nominal.
 *
 * @property indice Posición secuencial del rumbo en el círculo (0 a 31).
 * @property codigo Código abreviado internacional (ej: "N", "NpE", "NNE", etc.).
 * @property nombreCompleto Nombre en español tradicional náutico (ej: "Norte", "Norte por el Este", etc.).
 * @property anguloNominal Ángulo central o rumbo exacto del punto (0.0° a 348.75°).
 * @property rangoMinGrados Límite angular inferior del sector (inclusive).
 * @property rangoMaxGrados Límite angular superior del sector (exclusive).
 * @property cuadrante Cuadrante al que pertenece el punto ([CuadranteRosa]).
 */
data class PuntoRosaVientos(
    val indice: Int,
    val codigo: String,
    val nombreCompleto: String,
    val anguloNominal: Double,
    val rangoMinGrados: Double,
    val rangoMaxGrados: Double,
    val cuadrante: CuadranteRosa
) {
    init {
        require(indice in 0 until TOTAL_PUNTOS) {
            "El índice del rumbo debe estar entre 0 y ${TOTAL_PUNTOS - 1} (recibido: $indice)."
        }
        require(codigo.isNotBlank()) { "El código del rumbo no puede estar en blanco." }
        require(nombreCompleto.isNotBlank()) { "El nombre del rumbo no puede estar en blanco." }
        require(anguloNominal in 0.0..360.0) {
            "El ángulo nominal debe estar entre 0.0 y 360.0 grados (recibido: $anguloNominal)."
        }
    }

    /**
     * Determina si un ángulo en grados cae dentro del sector de 11.25° correspondiente a este rumbo.
     * Considera el caso especial del Norte (N), cuyo sector cruza el origen (354.375° a 5.625°).
     *
     * @param grados Ángulo azimutal a verificar.
     * @return `true` si el ángulo se ubica en este sector de 11.25°.
     */
    fun abarcaAngulo(grados: Double): Boolean {
        val normalizado = ((grados % 360.0) + 360.0) % 360.0
        return if (rangoMinGrados > rangoMaxGrados) {
            // Sector que cruza la discontinuidad 360°/0° (Norte)
            normalizado >= rangoMinGrados || normalizado < rangoMaxGrados
        } else {
            normalizado >= rangoMinGrados && normalizado < rangoMaxGrados
        }
    }

    /**
     * Calcula la desviación angular (en grados) entre un ángulo dado y el centro nominal de este rumbo.
     * El resultado estará en el intervalo [-5.625°, +5.625°] para ángulos dentro del sector.
     *
     * @param grados Ángulo a comparar.
     * @return Diferencia en grados sexagesimales (positiva si está al sentido horario, negativa al antihorario).
     */
    fun calcularDesviacion(grados: Double): Double {
        val normalizado = ((grados % 360.0) + 360.0) % 360.0
        var diff = normalizado - anguloNominal
        while (diff > 180.0) diff -= 360.0
        while (diff < -180.0) diff += 360.0
        return diff
    }

    /**
     * Formatea el intervalo angular en formato legible (ej: "354.38° - 5.63°").
     */
    fun formatearRango(): String {
        return "%.2f° - %.2f°".format(rangoMinGrados, rangoMaxGrados).replace(',', '.')
    }

    /**
     * Retorna una línea descriptiva formateada con el código, nombre, rumbo nominal y rango.
     */
    fun descripcionDetallada(): String {
        return "%-5s (%-24s): %6.2f° [Rango: %s]".format(
            codigo,
            nombreCompleto,
            anguloNominal,
            formatearRango()
        ).replace(',', '.')
    }

    companion object {
        const val TOTAL_PUNTOS: Int = 32
        const val AMPLITUD_PUNTO_GRADOS: Double = 11.25 // 360.0 / 32
        const val SEMI_AMPLITUD_GRADOS: Double = AMPLITUD_PUNTO_GRADOS / 2.0 // 5.625°
    }
}
