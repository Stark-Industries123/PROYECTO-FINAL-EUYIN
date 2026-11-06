package fisicas.motor

import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
 * Representa y actualiza el estado cinemático tridimensional instantáneo (posición y velocidad)
 * de un proyectil durante la integración numérica por pasos diferenciales.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es evolucionar las coordenadas
 *   y velocidades 3D bajo aceleración e impacto contra el suelo.
 */
class EstadoCinematica3D(
    velocidadInicial: Double,
    anguloGrados: Double,
    private val dt: Double,
    private val coeficienteRestitucion: Double
) {
    private var x: Double = 0.0
    private var y: Double = 0.0
    private var z: Double = 0.0
    private var vx: Double = if (abs(anguloGrados - 90.0) < 1e-9) 0.0 else velocidadInicial * cos(Math.toRadians(anguloGrados))
    private var vy: Double = velocidadInicial * sin(Math.toRadians(anguloGrados))
    private var vz: Double = 0.0

    /**
     * Evalúa las aceleraciones aerodinámica y gravitatoria e integra un diferencial de tiempo [dt].
     */
    fun avanzarSubPaso(g: Double, factorArrastre: Double, vientoX: Double, vientoZ: Double): Double {
        vx += -factorArrastre * (vx - vientoX) * dt
        vy += (-g - factorArrastre * vy) * dt
        vz += -factorArrastre * (vz - vientoZ) * dt
        x += vx * dt
        y += vy * dt
        z += vz * dt
        return y
    }

    /**
     * Registra las alturas máximas y evalúa el contacto con el suelo.
     * Retorna `true` si el proyectil alcanzó el estado de reposo definitivo.
     */
    fun registrarImpacto(piques: MutableList<Double>, metricas: DoubleArray): Boolean {
        metricas[0] = max(metricas[0], y)
        metricas[1] = max(metricas[1], y)
        val contacto = evaluarContactoSuelo()
        if (contacto == 0) return false
        if (metricas[1] > 1e-4) piques.add(metricas[1])
        metricas[1] = 0.0
        if (contacto == 1) metricas[2] += 1.0
        return contacto == -1 && vy == 0.0
    }

    /**
     * Verifica si el proyectil tocó el suelo (Y <= 0) con velocidad descendente.
     * Retorna 0 si sigue en el aire, 1 si rebota y -1 si queda en reposo.
     */
    fun evaluarContactoSuelo(): Int {
        if (y > 0.0) return 0
        y = 0.0
        if (vy >= 0.0) return 0
        return resolverImpactoVertical()
    }

    private fun resolverImpactoVertical(): Int {
        if (abs(vy) > 0.2 && coeficienteRestitucion > 0.0) {
            vy = -vy * coeficienteRestitucion
            return 1
        }
        vx = 0.0
        vy = 0.0
        vz = 0.0
        return -1
    }

    /**
     * Construye una instantánea inmutable [PuntoTrayectoria] para el instante indicado.
     */
    fun crearPunto(tiempo: Double): PuntoTrayectoria {
        return PuntoTrayectoria(tiempo = tiempo, x = x, y = max(0.0, y), z = z, vx = vx, vy = vy, vz = vz)
    }
}
