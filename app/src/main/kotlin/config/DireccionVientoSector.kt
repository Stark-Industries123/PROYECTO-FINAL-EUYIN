package config

/**
 * Representa los sectores cardinales e intercardinales de la rosa de los vientos
 * para modelar la dirección del viento (brújula / azimut).
 *
 * Cada sector comprende un rango exacto de grados en el intervalo [0°, 360°):
 * - [NORTE]: 330° a 30° (cruza el límite de 360°/0°)
 * - [NORESTE]: 30° a 60°
 * - [ESTE]: 60° a 120°
 * - [SURESTE]: 120° a 150°
 * - [SUR]: 150° a 210°
 * - [SUROESTE]: 210° a 240°
 * - [OESTE]: 240° a 300°
 * - [NOROESTE]: 300° a 330°
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es representar y clasificar
 *   las direcciones cardinales del viento según su azimut en grados.
 * - Inmutabilidad (POO): Enum inmutable, libre de efectos secundarios y seguro en concurrencia.
 *
 * @property descripcion Nombre legible del sector cardinal.
 * @property rangoMinGrados Límite inferior del rango en grados (inclusive, excepto en sectores continuos).
 * @property rangoMaxGrados Límite superior del rango en grados.
 */
enum class DireccionVientoSector(
    val descripcion: String,
    val rangoMinGrados: Double,
    val rangoMaxGrados: Double
) {
    NORTE("Norte", 330.0, 30.0),
    NORESTE("Noreste", 30.0, 60.0),
    ESTE("Este", 60.0, 120.0),
    SURESTE("Sureste", 120.0, 150.0),
    SUR("Sur", 150.0, 210.0),
    SUROESTE("Suroeste", 210.0, 240.0),
    OESTE("Oeste", 240.0, 300.0),
    NOROESTE("Noroeste", 300.0, 330.0);

    companion object {
        /**
         * Determina el sector cardinal correspondiente a partir de un ángulo en grados (0.0° a 360.0°).
         * Contempla que el sector NORTE cruza el límite de 360°/0° (de 330° a 30°).
         *
         * Si el ángulo recibido se encuentra fuera de [0, 360), se normaliza mediante módulo 360°.
         *
         * @param grados Ángulo azimutal de la dirección del viento en grados.
         * @return [DireccionVientoSector] correspondiente.
         */
        fun desdeGrados(grados: Double): DireccionVientoSector {
            val normalizado = ((grados % 360.0) + 360.0) % 360.0

            return when {
                normalizado >= 330.0 || normalizado < 30.0 -> NORTE
                normalizado < 60.0 -> NORESTE
                normalizado < 120.0 -> ESTE
                normalizado < 150.0 -> SURESTE
                normalizado < 210.0 -> SUR
                normalizado < 240.0 -> SUROESTE
                normalizado < 300.0 -> OESTE
                else -> NOROESTE
            }
        }
    }
}
