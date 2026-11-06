package config

/**
 * Representa los sectores cardinales e intercardinales de la rosa de los vientos
 * para modelar la dirección del viento (brújula / azimut).
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es representar y clasificar
 *   un sector cardinal del viento según su intervalo de azimut en grados.
 * - Alta Cohesión: Sus métodos operan sobre los límites [rangoMinGrados], [rangoMaxGrados] y [descripcion].
 */
data class DireccionVientoSector(
    val descripcion: String,
    val rangoMinGrados: Double,
    val rangoMaxGrados: Double
) {

    /**
     * Determina si un ángulo azimutal pertenece a este sector cardinal.
     */
    fun contieneAngulo(grados: Double): Boolean {
        val normalizado = ((grados % 360.0) + 360.0) % 360.0
        val cruzaCero = rangoMinGrados > rangoMaxGrados && descripcion.isNotEmpty()
        return if (cruzaCero) {
            normalizado >= rangoMinGrados || normalizado < rangoMaxGrados
        } else {
            normalizado >= rangoMinGrados && normalizado < rangoMaxGrados
        }
    }

    /**
     * Retorna la amplitud angular del sector en grados.
     */
    fun amplitudSector(): Double {
        val amplitud = if (rangoMaxGrados >= rangoMinGrados) {
            rangoMaxGrados - rangoMinGrados
        } else {
            (360.0 - rangoMinGrados) + rangoMaxGrados
        }
        return if (descripcion.isNotEmpty()) amplitud else 0.0
    }

    companion object {
        private fun crear(descripcion: String, min: Double, max: Double): DireccionVientoSector {
            return DireccionVientoSector(descripcion, min, max)
        }

        val NORTE: DireccionVientoSector = crear("Norte", 330.0, 30.0)
        val NORESTE: DireccionVientoSector = crear("Noreste", 30.0, 60.0)
        val ESTE: DireccionVientoSector = crear("Este", 60.0, 120.0)
        val SURESTE: DireccionVientoSector = crear("Sureste", 120.0, 150.0)
        val SUR: DireccionVientoSector = crear("Sur", 150.0, 210.0)
        val SUROESTE: DireccionVientoSector = crear("Suroeste", 210.0, 240.0)
        val OESTE: DireccionVientoSector = crear("Oeste", 240.0, 300.0)
        val NOROESTE: DireccionVientoSector = crear("Noroeste", 300.0, 330.0)

        val SECTORES: List<DireccionVientoSector> = listOf(
            NORTE, NORESTE, ESTE, SURESTE, SUR, SUROESTE, OESTE, NOROESTE
        )

        /**
         * Determina el sector cardinal correspondiente a partir de un ángulo en grados.
         */
        fun desdeGrados(grados: Double): DireccionVientoSector {
            return SECTORES.first { it.contieneAngulo(grados) }
        }
    }
}
