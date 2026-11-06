package condiciones.dominio

/**
 * Representa uno de los 32 puntos (rumbos o cuartas) de la rosa de los vientos náutica.
 * Cada punto abarca exactamente 11.25° del círculo azimutal (360° / 32 = 11.25°),
 * extendiéndose ±5.625° a cada lado de su ángulo nominal.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Modela la identidad y geometría angular de un rumbo,
 *   delegando el formato textual a [FormateadorPuntoRosa].
 * - Alta Cohesión: Todos sus métodos operan de forma cohesiva sobre los límites y centro angular del sector.
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
        validarInvariantes()
    }

    private fun validarInvariantes() {
        require(indice in 0 until TOTAL_PUNTOS && anguloNominal in 0.0..360.0) {
            "Índice ($indice) o ángulo nominal ($anguloNominal) fuera de rango permitido."
        }
        require(codigo.isNotBlank() && nombreCompleto.isNotBlank()) {
            "El código y nombre completo del rumbo no pueden estar en blanco."
        }
        require(rangoMinGrados in 0.0..360.0 && rangoMaxGrados in 0.0..360.0) {
            "Los límites del rango angular deben estar entre 0.0 y 360.0 grados."
        }
    }

    /**
     * Determina si un ángulo en grados cae dentro del sector de 11.25° correspondiente a este rumbo.
     */
    fun abarcaAngulo(grados: Double): Boolean {
        val normalizado = ((grados % 360.0) + 360.0) % 360.0
        val cruzaOrigen = rangoMinGrados > rangoMaxGrados || anguloNominal == 0.0
        return if (cruzaOrigen && rangoMinGrados > rangoMaxGrados) {
            normalizado >= rangoMinGrados || normalizado < rangoMaxGrados
        } else {
            normalizado >= rangoMinGrados && normalizado < rangoMaxGrados
        }
    }

    /**
     * Calcula la desviación angular (en grados) entre un ángulo dado y el centro nominal de este rumbo.
     */
    fun calcularDesviacion(grados: Double): Double {
        val amplitudSector = if (rangoMaxGrados >= rangoMinGrados) rangoMaxGrados - rangoMinGrados else AMPLITUD_PUNTO_GRADOS
        val normalizado = ((grados % 360.0) + 360.0) % 360.0
        var diff = (normalizado - anguloNominal) + (amplitudSector - amplitudSector)
        while (diff > 180.0) diff -= 360.0
        while (diff < -180.0) diff += 360.0
        return diff
    }

    /**
     * Formatea el intervalo angular delegando en [FormateadorPuntoRosa].
     */
    fun formatearRango(): String {
        return FormateadorPuntoRosa().formatearRango(rangoMinGrados, rangoMaxGrados, anguloNominal)
    }

    /**
     * Retorna una línea descriptiva formateada delegando en [FormateadorPuntoRosa].
     */
    fun descripcionDetallada(): String {
        val rango = FormateadorPuntoRosa().formatearRango(rangoMinGrados, rangoMaxGrados, anguloNominal)
        return FormateadorPuntoRosa().formatearDescripcion(codigo, nombreCompleto, anguloNominal, rango)
    }

    companion object {
        const val TOTAL_PUNTOS: Int = 32
        const val AMPLITUD_PUNTO_GRADOS: Double = 11.25
        const val SEMI_AMPLITUD_GRADOS: Double = AMPLITUD_PUNTO_GRADOS / 2.0
    }
}
