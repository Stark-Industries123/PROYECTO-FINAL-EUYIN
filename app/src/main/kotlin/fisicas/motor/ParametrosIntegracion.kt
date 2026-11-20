package fisicas.motor

import kotlin.math.max

/**
 * Objeto de valor inmutable que agrupa los parámetros numéricos y físicos de configuración
 * para la integración cinemática tridimensional.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Centraliza la configuración, validación y
 *   derivación de pasos temporales y arrastre para que [IntegradorCinematica3D] dependa
 *   de un único objeto de configuración cohesivo.
 * - Inmutabilidad y Encapsulamiento: Todas sus propiedades son de solo lectura (`val`).
 *
 * @property coeficienteRestitucion Coeficiente de restitución en rebotes contra el suelo [0.0, 1.0].
 * @property coeficienteArrastre Coeficiente de arrastre aerodinámico (>= 0.0).
 * @property pasoTiempoMuestreo Intervalo temporal entre muestras consecutivas en segundos (> 0.0).
 * @property tiempoMaximo Tiempo límite máximo de simulación en segundos (> 0.0).
 */
data class ParametrosIntegracion(
    val coeficienteRestitucion: Double = DEFAULT_COEFICIENTE_RESTITUCION,
    val coeficienteArrastre: Double = DEFAULT_COEFICIENTE_ARRASTRE,
    val pasoTiempoMuestreo: Double = DEFAULT_PASO_TIEMPO_MUESTREO,
    val tiempoMaximo: Double = DEFAULT_TIEMPO_MAXIMO
) {
    init {
        require(coeficienteRestitucion in 0.0..1.0) {
            "El coeficiente de restitución debe estar entre 0.0 y 1.0 (recibido: $coeficienteRestitucion)."
        }
        require(coeficienteArrastre >= 0.0) {
            "El coeficiente de arrastre aerodinámico debe ser mayor o igual a 0 (recibido: $coeficienteArrastre)."
        }
        require(pasoTiempoMuestreo > 0.0) {
            "El paso de tiempo de muestreo debe ser estrictamente mayor a 0 (recibido: $pasoTiempoMuestreo)."
        }
        require(tiempoMaximo > 0.0) {
            "El tiempo máximo de simulación debe ser estrictamente mayor a 0 (recibido: $tiempoMaximo)."
        }
    }

    /**
     * Calcula la cantidad de sub-pasos diferenciales por ventana de muestreo.
     */
    fun calcularSubPasos(): Int = max(1, (pasoTiempoMuestreo / PASO_SUBINTEGRACION).toInt())

    /**
     * Calcula el diferencial de tiempo `dt` correspondiente a cada sub-paso de integración.
     */
    fun calcularDeltaTiempo(): Double = pasoTiempoMuestreo / calcularSubPasos()

    /**
     * Calcula el factor de arrastre aerodinámico efectivo para la masa dada.
     */
    fun calcularFactorArrastre(masaKg: Double): Double {
        require(masaKg > 0.0) {
            "La masa del proyectil debe ser estrictamente mayor a 0 kg (recibido: $masaKg)."
        }
        return coeficienteArrastre / masaKg
    }

    companion object {
        const val DEFAULT_COEFICIENTE_RESTITUCION: Double = 0.75
        const val DEFAULT_COEFICIENTE_ARRASTRE: Double = 0.05
        const val DEFAULT_PASO_TIEMPO_MUESTREO: Double = 0.1
        const val DEFAULT_TIEMPO_MAXIMO: Double = 300.0
        const val PASO_SUBINTEGRACION: Double = 0.001
    }
}
