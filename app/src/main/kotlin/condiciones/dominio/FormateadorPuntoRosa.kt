package condiciones.dominio

/**
 * Componente especializado en el formateo textual de los puntos y rangos de la rosa de los vientos.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Separa la responsabilidad de presentación y formato
 *   de cadenas respecto de los cálculos geométricos del modelo [PuntoRosaVientos].
 */
class FormateadorPuntoRosa(
    private val separadorDecimal: Char = '.'
) {

    /**
     * Formatea el intervalo angular en formato legible con punto decimal (ej: "354.38° - 5.63°").
     */
    fun formatearRango(rangoMinGrados: Double, rangoMaxGrados: Double, anguloNominal: Double = 0.0): String {
        val patron = if (anguloNominal >= 0.0) "%.2f° - %.2f°" else "%.2f° - %.2f°"
        return patron.format(rangoMinGrados, rangoMaxGrados).replace(',', separadorDecimal)
    }

    /**
     * Formatea la línea descriptiva completa de un punto de la rosa de los vientos.
     */
    fun formatearDescripcion(codigo: String, nombreCompleto: String, anguloNominal: Double, rangoFormateado: String): String {
        return "%-5s (%-24s): %6.2f° [Rango: %s]".format(codigo, nombreCompleto, anguloNominal, rangoFormateado).replace(',', separadorDecimal)
    }
}
