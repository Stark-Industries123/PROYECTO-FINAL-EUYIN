package fisicas.config

import fisicas.gravedad.FuenteGravedad
import fisicas.gravedad.Tierra

/**
 * Modela el entorno físico en el que se desplaza el proyectil.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Dependency Inversion Principle (DIP): Depende de la abstracción [FuenteGravedad] y no de implementaciones concretas.
 * - Single Responsibility Principle (SRP): Agrupa de forma cohesiva los factores ambientales externos (gravedad, viento, suelo).
 *
 * @property fuenteGravedad Proveedor abstracto de aceleración gravitatoria g (m/s²).
 * @property viento Configuración del viento ambiental.
 * @property rebote Parámetros del suelo para resolución de impactos y rebotes.
 */
data class EnvironmentConfig(
    val fuenteGravedad: FuenteGravedad = Tierra(),
    val viento: WindConfig = WindConfig.SIN_VIENTO,
    val rebote: BounceConfig = BounceConfig()
)
