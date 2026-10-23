package fisicas.config

import fisicas.vector.Vector2D
import kotlin.math.cos
import kotlin.math.sin

/**
 * Modela la configuración inmutable del viento según el sistema de navegación por brújula.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Modela exclusivamente los parámetros y la proyección vectorial
 *   del viento en el sistema de coordenadas.
 * - Inmutabilidad (POO): Valores de solo lectura `val` con validaciones robustas en el bloque `init`.
 * - Cohesión: Contiene el método [aVector] para transformar los parámetros de navegación a la representación matemática [Vector2D].
 *
 * Convención de Brújula:
 * - Norte = 0° (apunta hacia arriba: +Y)
 * - Este = 90° (apunta hacia la derecha: +X)
 * - Sur = 180° (apunta hacia abajo: -Y)
 * - Oeste = 270° (apunta hacia la izquierda: -X)
 *
 * @property magnitud Magnitud de la velocidad/fuerza del viento (debe ser >= 0.0).
 * @property anguloGrados Ángulo de origen/rumbo del viento en grados de brújula (debe estar entre 0.0 y 360.0).
 */
data class WindConfig(
    val magnitud: Double,
    val anguloGrados: Double
) {
    init {
        require(magnitud >= 0.0) {
            "La magnitud del viento debe ser mayor o igual a 0.0 (recibido: $magnitud)."
        }
        require(anguloGrados in 0.0..360.0) {
            "El ángulo de dirección del viento debe estar entre 0.0 y 360.0 grados (recibido: $anguloGrados)."
        }
    }

    /**
     * Proyecta la magnitud y dirección del viento a un [Vector2D] cartesiano (+X derecha, +Y arriba):
     * - Vx = magnitud * sin(angulo_rad)
     * - Vy = magnitud * cos(angulo_rad)
     */
    fun aVector(): Vector2D {
        val anguloRad = anguloGrados * (Math.PI / 180.0)
        val vx = magnitud * sin(anguloRad)
        val vy = magnitud * cos(anguloRad)
        return Vector2D(vx, vy)
    }

    /**
     * Propiedad de conveniencia para acceder al vector proyectado.
     */
    val vector: Vector2D get() = aVector()

    companion object {
        val SIN_VIENTO = WindConfig(0.0, 0.0)
    }
}
