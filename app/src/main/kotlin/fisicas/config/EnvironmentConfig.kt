package fisicas.config

import fisicas.gravedad.FuenteGravedad
import fisicas.gravedad.Tierra

/**
 * Modela el entorno físico en el que se desplaza el proyectil.
 *
 * @property fuenteGravedad Proveedor abstracto de aceleración gravitatoria g (m/s²).
 * @property viento Configuración del viento ambiental.
 * @property rebote Parámetros del suelo para resolución de impactos y rebotes.
 */
data class EnvironmentConfig(
    val fuenteGravedad: FuenteGravedad = Tierra(),
    val viento: WindConfig = WindConfig.SIN_VIENTO,
    val rebote: BounceConfig = BounceConfig()
) {
    /**
     * Retorna la magnitud de aceleración gravitatoria del entorno en m/s².
     */
    fun obtenerGravedad(): Double = fuenteGravedad.gravedad

    companion object {
        fun conVientoYRebote(
            magnitudViento: Double,
            anguloViento: Double,
            restitucion: Double,
            friccion: Double
        ): EnvironmentConfig = EnvironmentConfig(
            viento = WindConfig(magnitud = magnitudViento, anguloGrados = anguloViento),
            rebote = BounceConfig(coeficienteRestitucion = restitucion, friccionSuelo = friccion, umbralReposo = 0.2)
        )
    }
}
