package fisicas.fuerzas

import kotlin.math.pow
import kotlin.math.sign

/**
 * Implementación dedicada al cálculo de la fuerza de arrastre aerodinámico cuadrático
 * y la aceleración inducida por el viento según la masa del proyectil.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es evaluar la ecuación
 *   aerodinámica Fx = 0.5 * rho * (v_viento - vx)^2 * Cd * A y la relación inercial ax = Fx / m.
 * - Dependency Inversion Principle (DIP): Implementa la abstracción [CalculadorFuerzaAerodinamica].
 * - Encapsulamiento e Inmutabilidad (POO): Propiedades físicas privadas e inmutables con validación.
 *
 * @property densidadAire Densidad del fluido rho en kg/m³ (debe ser >= 0.0).
 * @property coeficienteArrastre Coeficiente adimensional de arrastre Cd (debe ser >= 0.0).
 * @property areaTransversal Área de sección transversal A en m² (debe ser >= 0.0).
 */
class CalculadorArrastreAerodinamico(
    private val densidadAire: Double = DEFAULT_DENSIDAD_AIRE,
    private val coeficienteArrastre: Double = DEFAULT_COEFICIENTE_ARRASTRE,
    private val areaTransversal: Double = DEFAULT_AREA_TRANSVERSAL
) : CalculadorFuerzaAerodinamica {

    init {
        require(densidadAire >= 0.0) {
            "La densidad del aire debe ser mayor o igual a 0.0 (recibido: $densidadAire)."
        }
        require(coeficienteArrastre >= 0.0) {
            "El coeficiente de arrastre debe ser mayor o igual a 0.0 (recibido: $coeficienteArrastre)."
        }
        require(areaTransversal >= 0.0) {
            "El área transversal debe ser mayor o igual a 0.0 (recibido: $areaTransversal)."
        }
    }

    fun obtenerDensidadAire(): Double = densidadAire
    fun obtenerCoeficienteArrastre(): Double = coeficienteArrastre
    fun obtenerAreaTransversal(): Double = areaTransversal

    /**
     * Evalúa la fuerza de arrastre mediante la ecuación cuadrática:
     * Fx = 0.5 * rho * (v_viento - vx)^2 * Cd * A, preservando el sentido de (v_viento - vx)
     * para permitir dinámicamente el retroceso (X < 0) ante viento fuerte en contra.
     */
    override fun calcularFuerzaArrastre(velocidadViento: Double, velocidadProyectil: Double): Double {
        val velocidadRelativa = velocidadViento - velocidadProyectil
        val magnitudFuerza = 0.5 * densidadAire * velocidadRelativa.pow(2) * coeficienteArrastre * areaTransversal
        return magnitudFuerza * sign(velocidadRelativa)
    }

    /**
     * Sobrecarga que calcula la fuerza de arrastre a partir de la velocidad relativa ya computada.
     */
    fun calcularFuerzaArrastre(velocidadRelativa: Double): Double {
        return calcularFuerzaArrastre(velocidadRelativa, 0.0)
    }

    /**
     * Determina la aceleración aerodinámica como ax = Fx / m.
     */
    override fun calcularAceleracion(velocidadViento: Double, velocidadProyectil: Double, masa: Double): Double {
        require(masa > 0.0) {
            "La masa del proyectil debe ser estrictamente mayor a 0.0 kg (recibido: $masa)."
        }
        val fx = calcularFuerzaArrastre(velocidadViento, velocidadProyectil)
        return fx / masa
    }

    companion object {
        const val DEFAULT_DENSIDAD_AIRE: Double = 1.225
        const val DEFAULT_COEFICIENTE_ARRASTRE: Double = 0.05
        const val DEFAULT_AREA_TRANSVERSAL: Double = 0.5
    }
}
