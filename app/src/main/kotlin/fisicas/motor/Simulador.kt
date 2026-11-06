package fisicas.motor

import config.ConfigSimulacion
import fisicas.gravedad.FuenteGravedad

/**
 * Motor físico encargado de simular la dinámica tridimensional del tiro de un proyectil
 * incorporando gravedad, modelo de arrastre aerodinámico y viento 360°, y mecánica de
 * rebote contra el suelo.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es validar los coeficientes
 *   físicos del simulador y coordinar el cálculo de trayectoria delegando la integración
 *   numérica a [IntegradorCinematica3D].
 */
class Simulador(
    private val gravedadFoco: FuenteGravedad,
    private val coeficienteRestitucion: Double = 0.75,
    private val coeficienteArrastre: Double = 0.05,
    private val pasoTiempoMuestreo: Double = 0.1
) : CalculadorTrayectoria {

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
    }

    /**
     * Calcula la trayectoria completa del proyectil a partir del objeto inmutable [ConfigSimulacion].
     */
    override fun calcularTrayectoria(config: ConfigSimulacion): ResultadoSimulacion {
        if (config.velocidadInicial <= 0.0 || config.angulo <= 0.0 || gravedadFoco.gravedad <= 0.0) {
            val puntoInicial = PuntoTrayectoria(0.0, 0.0, 0.0)
            return ResultadoSimulacion(0.0, 0.0, 0.0, listOf(puntoInicial))
        }
        val integrador = IntegradorCinematica3D(coeficienteRestitucion, coeficienteArrastre, pasoTiempoMuestreo)
        return integrador.simular(config)
    }

    /**
     * Sobrecarga de conveniencia que construye una [ConfigSimulacion] base con [gravedadFoco].
     */
    fun calcularTrayectoria(velocidadInicial: Double, anguloGrados: Double): ResultadoSimulacion {
        val config = ConfigSimulacion(
            velocidadInicial = velocidadInicial,
            angulo = anguloGrados,
            fuenteGravedad = gravedadFoco
        )
        return calcularTrayectoria(config)
    }
}
