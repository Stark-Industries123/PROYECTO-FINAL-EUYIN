package condiciones.dominio

/**
 * Modelo de dominio que representa el proyectil utilizado en el tiro parabólico.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Modela exclusivamente los atributos físicos
 *   intrínsecos del cuerpo lanzado (peso/masa) y sus conversiones de unidades y cálculo de energía.
 * - Inmutabilidad (POO): Atributos inmutables con validación de invariantes físicas en construcción.
 * - Encapsulación: Reglas físicas encapsuladas dentro del objeto de dominio.
 *
 * @property pesoKg Peso / masa del proyectil expresado en kilogramos (debe ser estrictamente mayor a 0).
 */
data class Proyectil(
    val pesoKg: Double
) {
    init {
        require(pesoKg > 0.0) {
            "El peso del proyectil debe ser estrictamente mayor a 0 kg (recibido: $pesoKg)."
        }
    }

    /**
     * Retorna el peso del proyectil convertido a gramos (g).
     */
    fun pesoEnGramos(): Double = pesoKg * FACTOR_KG_A_G

    /**
     * Retorna el peso del proyectil convertido a libras (lb).
     */
    fun pesoEnLibras(): Double = pesoKg * FACTOR_KG_A_LB

    /**
     * Calcula la energía cinética que adquiere el proyectil para una rapidez dada en m/s:
     * Ec = (1/2) * m * v²
     *
     * @param rapidezMS Rapidez inicial o instantánea en metros por segundo (>= 0.0).
     * @return Energía cinética en Joules (J).
     */
    fun calcularEnergiaCinetica(rapidezMS: Double): Double {
        require(rapidezMS >= 0.0) {
            "La rapidez para el cálculo cinético debe ser mayor o igual a 0 m/s (recibido: $rapidezMS)."
        }
        return 0.5 * pesoKg * rapidezMS * rapidezMS
    }

    /**
     * Retorna una descripción resumida y formateada del proyectil.
     */
    fun descripcionFormateada(): String {
        return "Proyectil(peso = %.2f kg / %.1f g)".format(pesoKg, pesoEnGramos())
    }

    companion object {
        const val FACTOR_KG_A_G: Double = 1000.0
        const val FACTOR_KG_A_LB: Double = 2.20462
    }
}
