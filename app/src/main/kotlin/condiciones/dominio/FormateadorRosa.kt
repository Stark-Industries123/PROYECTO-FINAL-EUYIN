package condiciones.dominio

/**
 * Contrato de abstracción para el formateo textual de puntos y rangos de la rosa de los vientos.
 */
interface FormateadorRosa {

    /**
     * Formatea el intervalo angular en formato legible.
     */
    fun formatearRango(rangoMinGrados: Double, rangoMaxGrados: Double, anguloNominal: Double = 0.0): String

    /**
     * Formatea la línea descriptiva completa de un punto de la rosa de los vientos.
     */
    fun formatearDescripcion(codigo: String, nombreCompleto: String, anguloNominal: Double, rangoFormateado: String): String
}
