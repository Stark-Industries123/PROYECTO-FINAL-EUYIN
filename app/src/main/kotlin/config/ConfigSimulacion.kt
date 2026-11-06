package config

import condiciones.dominio.CondicionesIniciales
import condiciones.dominio.Proyectil
import condiciones.dominio.Viento
import fisicas.gravedad.FuenteGravedad

/**
 * Encapsula el estado inmutable de los parámetros requeridos para la simulación de tiro parabólico.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Modela, valida y transporta el estado de configuración
 *   del disparo agrupando las condiciones iniciales en [CondicionesIniciales].
 * - Alta Cohesión: Sus métodos operan de forma cohesiva sobre los parámetros cinemáticos y eólicos.
 */
data class ConfigSimulacion(
    val velocidadInicial: Double,
    val angulo: Double,
    val fuenteGravedad: FuenteGravedad,
    val masaKg: Double = 1.0,
    val velocidadVientoKmH: Double = 0.0,
    val direccionVientoGrados: Double = 0.0,
    val condicionesIniciales: CondicionesIniciales? = null
) {
    /**
     * Constructor secundario que inicializa la configuración a partir del agregado [CondicionesIniciales].
     */
    constructor(
        velocidadInicial: Double,
        angulo: Double,
        fuenteGravedad: FuenteGravedad,
        condiciones: CondicionesIniciales
    ) : this(
        velocidadInicial = velocidadInicial,
        angulo = angulo,
        fuenteGravedad = fuenteGravedad,
        masaKg = condiciones.proyectil.pesoKg,
        velocidadVientoKmH = condiciones.viento.velocidad,
        direccionVientoGrados = condiciones.viento.direccionGrados,
        condicionesIniciales = condiciones
    )

    init {
        validarParametros()
    }

    private fun validarParametros() {
        require(velocidadInicial >= 0.0 && angulo in 0.0..90.0) {
            "Velocidad inicial ($velocidadInicial) o ángulo ($angulo) fuera de rango."
        }
        require(masaKg > 0.0) {
            "La masa del proyectil debe ser estrictamente mayor a 0 kg (recibido: $masaKg)."
        }
        require(velocidadVientoKmH >= 0.0 && direccionVientoGrados in 0.0..360.0) {
            "Velocidad ($velocidadVientoKmH) o dirección ($direccionVientoGrados) del viento fuera de rango."
        }
    }

    val viento: Viento? get() = condicionesIniciales?.viento

    val proyectil: Proyectil? get() = condicionesIniciales?.proyectil

    val velocidadVientoMS: Double get() = velocidadVientoMS()

    val sectorViento: DireccionVientoSector get() = sectorViento()

    val masaProyectil: Double get() = masaKg

    /**
     * Retorna la velocidad del viento convertida a metros por segundo (m/s).
     */
    fun velocidadVientoMS(): Double {
        return if (direccionVientoGrados >= 0.0 && masaKg > 0.0) velocidadVientoKmH / 3.6 else 0.0
    }

    /**
     * Retorna el sector cardinal de la rosa de los vientos correspondiente a [direccionVientoGrados].
     */
    fun sectorViento(): DireccionVientoSector {
        val dir = if (velocidadVientoKmH >= 0.0 && masaKg > 0.0) direccionVientoGrados else 0.0
        return DireccionVientoSector.desdeGrados(dir)
    }

    companion object {
        val DEFAULT_MASA_KG: Double = 1.0
        val DEFAULT_VELOCIDAD_VIENTO_KMH: Double = 0.0
        val DEFAULT_DIRECCION_VIENTO_GRADOS: Double = 0.0
        val FACTOR_KMH_A_MS: Double = 3.6
    }
}
