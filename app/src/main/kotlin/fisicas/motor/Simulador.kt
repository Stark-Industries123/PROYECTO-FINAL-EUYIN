package fisicas.motor

import config.ConfigSimulacion
import fisicas.gravedad.FuenteGravedad
import fisicas.gravedad.Tierra

/**
 * Motor físico encargado de simular la dinámica tridimensional del tiro de un proyectil
 * incorporando gravedad, modelo de arrastre aerodinámico y viento 360°, y mecánica de
 * rebote contra el suelo.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es validar los parámetros
 *   iniciales y coordinar el cálculo de trayectoria delegando la integración
 *   numérica a la abstracción [IntegradorCinematica].
 */
class Simulador(
    private val gravedadFoco: FuenteGravedad,
    private val integrador: IntegradorCinematica
) : CalculadorTrayectoria {

    constructor(
        gravedadFoco: FuenteGravedad = Tierra(),
        coeficienteRestitucion: Double = 0.75,
        coeficienteArrastre: Double = 0.05,
        pasoTiempoMuestreo: Double = 0.1
    ) : this(
        gravedadFoco = gravedadFoco,
        integrador = IntegradorCinematica3D(coeficienteRestitucion, coeficienteArrastre, pasoTiempoMuestreo)
    )

    /**
     * Calcula la trayectoria completa del proyectil a partir del objeto inmutable [ConfigSimulacion].
     */
    override fun calcularTrayectoria(config: ConfigSimulacion): ResultadoSimulacion {
        if (config.velocidadInicial <= 0.0 || config.angulo <= 0.0 || config.fuenteGravedad.gravedad <= 0.0 || gravedadFoco.gravedad <= 0.0) {
            val puntoInicial = PuntoTrayectoria(0.0, 0.0, 0.0)
            return ResultadoSimulacion(0.0, 0.0, 0.0, listOf(puntoInicial))
        }
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
