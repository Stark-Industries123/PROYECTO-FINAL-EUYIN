package fisicas.motor

import config.ConfigSimulacion
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin

/**
 * Representa y actualiza el estado cinemático tridimensional instantáneo (posición y velocidad)
 * de un proyectil durante la integración numérica por pasos diferenciales.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Evoluciona las coordenadas y velocidades 3D bajo
 *   gravedad, arrastre, viento y rebotes, delegando el almacenamiento de muestras y métricas
 *   al contrato [RegistroTrayectoria].
 * - Dependency Inversion Principle (DIP): Depende de la abstracción [RegistroTrayectoria]
 *   inyectada por constructor.
 */
class EstadoCinematica3D(
    config: ConfigSimulacion,
    parametros: ParametrosIntegracion,
    private val registro: RegistroTrayectoria
) : EstadoCinematica {

    private val dt: Double = parametros.calcularDeltaTiempo()
    private val coeficienteRestitucion: Double = parametros.coeficienteRestitucion
    private val gravedad: Double = config.fuenteGravedad.gravedad
    private val factorArrastre: Double = parametros.calcularFactorArrastre(config.masaKg)
    private val vientoX: Double = -config.velocidadVientoMS() * cos(config.direccionVientoGrados * PI / 180.0)
    private val vientoZ: Double = -config.velocidadVientoMS() * sin(config.direccionVientoGrados * PI / 180.0)

    private var x: Double = 0.0
    private var y: Double = 0.0
    private var z: Double = 0.0
    private var vx: Double = if (abs(config.angulo - 90.0) < 1e-9) {
        0.0
    } else {
        config.velocidadInicial * cos(config.angulo * PI / 180.0)
    }
    private var vy: Double = config.velocidadInicial * sin(config.angulo * PI / 180.0)
    private var vz: Double = 0.0

    /**
     * Evalúa las aceleraciones aerodinámica y gravitatoria, integra un diferencial de tiempo [dt]
     * y evalúa el impacto contra el suelo.
     *
     * @return `true` si el proyectil alcanzó el estado de reposo definitivo.
     */
    override fun avanzarSubPaso(): Boolean {
        vx += -factorArrastre * (vx - vientoX) * dt
        vy += (-gravedad - factorArrastre * vy) * dt
        vz += -factorArrastre * (vz - vientoZ) * dt
        x += vx * dt
        y += vy * dt
        z += vz * dt
        val alturaPreContacto = y
        val contacto = evaluarContactoSuelo()
        registro.registrarSubPaso(alturaPreContacto, contacto)
        return contacto == -1 && vy == 0.0
    }

    /**
     * Registra una muestra instantánea del estado cinemático en [registro] para el instante [tiempo].
     */
    override fun registrarMuestra(tiempo: Double) {
        val punto = PuntoTrayectoria(
            tiempo = tiempo,
            x = x,
            y = max(0.0, y),
            z = z,
            vx = vx,
            vy = vy,
            vz = vz
        )
        registro.registrarPunto(punto)
    }

    /**
     * Construye el resultado final delegando en [registro].
     */
    override fun construirResultado(): ResultadoSimulacion = registro.construirResultado()

    private fun evaluarContactoSuelo(): Int {
        if (y > 0.0) return 0
        y = 0.0
        if (vy >= 0.0) return 0
        if (abs(vy) > UMBRAL_VELOCIDAD_REBOTE && coeficienteRestitucion > 0.0) {
            vy = -vy * coeficienteRestitucion
            return 1
        }
        vx = 0.0
        vy = 0.0
        vz = 0.0
        return -1
    }

    companion object {
        private const val UMBRAL_VELOCIDAD_REBOTE: Double = 0.2
    }
}
