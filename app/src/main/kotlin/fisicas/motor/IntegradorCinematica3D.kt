package fisicas.motor

import config.ConfigSimulacion
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.round
import kotlin.math.sin

/**
 * Servicio de integración numérica tridimensional encargado de evolucionar el movimiento
 * del proyectil en el tiempo delegando el estado y acumulación en [FabricaSesionCinematica].
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Coordina exclusivamente el bucle temporal de muestreo
 *   y sub-integración para construir [ResultadoSimulacion].
 * - Dependency Inversion Principle (DIP): Depende de las abstracciones [FabricaSesionCinematica]
 *   y [SesionCinematica] inyectadas por constructor.
 */
class IntegradorCinematica3D(
    private val coeficienteArrastre: Double = 0.05,
    private val pasoTiempoMuestreo: Double = 0.1,
    private val fabricaSesion: FabricaSesionCinematica = FabricaSesionCinematica3D()
) : IntegradorCinematica {

    constructor(
        coeficienteRestitucion: Double,
        coeficienteArrastre: Double = 0.05,
        pasoTiempoMuestreo: Double = 0.1
    ) : this(
        coeficienteArrastre = coeficienteArrastre,
        pasoTiempoMuestreo = pasoTiempoMuestreo,
        fabricaSesion = FabricaSesionCinematica3D(coeficienteRestitucion)
    )

    init {
        require(coeficienteArrastre >= 0.0) {
            "El coeficiente de arrastre aerodinámico debe ser mayor o igual a 0 (recibido: $coeficienteArrastre)."
        }
        require(pasoTiempoMuestreo > 0.0) {
            "El paso de tiempo de muestreo debe ser estrictamente mayor a 0 (recibido: $pasoTiempoMuestreo)."
        }
    }

    /**
     * Ejecuta la integración numérica completa para la configuración dada.
     */
    override fun simular(config: ConfigSimulacion): ResultadoSimulacion {
        val subPasos = max(1, (pasoTiempoMuestreo / 0.001).toInt())
        val dt = pasoTiempoMuestreo / subPasos
        val sesion = fabricaSesion.crearSesion(config, dt)
        ejecutarSimulacion(config, sesion)
        return sesion.construirResultado()
    }

    private fun ejecutarSimulacion(config: ConfigSimulacion, sesion: SesionCinematica) {
        var tiempoActual = 0.0
        var enReposo = false
        while (!enReposo && tiempoActual < 300.0) {
            enReposo = ejecutarVentanaMuestreo(config, sesion)
            tiempoActual += pasoTiempoMuestreo
            sesion.capturarMuestra(round(tiempoActual * 1000.0) / 1000.0)
        }
    }

    private fun ejecutarVentanaMuestreo(config: ConfigSimulacion, sesion: SesionCinematica): Boolean {
        val subPasos = max(1, (pasoTiempoMuestreo / 0.001).toInt())
        val factorArrastre = coeficienteArrastre / config.masaKg
        val rad = Math.toRadians(config.direccionVientoGrados)
        val vientoX = -config.velocidadVientoMS() * cos(rad)
        val vientoZ = -config.velocidadVientoMS() * sin(rad)
        for (paso in 0 until subPasos) {
            if (sesion.avanzarSubPaso(config.fuenteGravedad.gravedad, factorArrastre, vientoX, vientoZ)) {
                return true
            }
        }
        return false
    }
}
