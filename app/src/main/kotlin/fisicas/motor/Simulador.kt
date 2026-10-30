package fisicas.motor

import config.ConfigSimulacion
import fisicas.fuerzas.CalculadorArrastreAerodinamico
import fisicas.fuerzas.CalculadorFuerzaAerodinamica
import fisicas.gravedad.FuenteGravedad
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.round
import kotlin.math.sin

/**
 * Orquestador del bucle de simulación física del tiro parabólico y ciclo continuo de rebotes.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Coordina el bucle temporal de la simulación y el ciclo
 *   de rebotes contra el suelo, delegando el cálculo de la fuerza de arrastre en [CalculadorFuerzaAerodinamica]
 *   y la integración numérica de cada paso en [MotorFisica2D]. No contiene lógica de consola.
 * - Dependency Inversion Principle (DIP): Implementa [CalculadorTrayectoria] y depende de las
 *   abstracciones [FuenteGravedad], [CalculadorFuerzaAerodinamica] y [MotorFisica2D].
 * - Open/Closed Principle (OCP): Permite inyectar motores de integración o modelos aerodinámicos
 *   alternativos sin modificar esta clase.
 *
 * @property gravedadFoco Abstracción que provee la aceleración gravitacional por defecto.
 * @property coeficienteRestitucion Fracción de velocidad vertical preservada tras impactar el suelo (0.0 a 1.0).
 * @property coeficienteArrastre Coeficiente adimensional de resistencia aerodinámica Cd.
 * @property pasoTiempoMuestreo Intervalo temporal en segundos entre puntos muestreados en la trayectoria.
 * @property densidadAire Densidad del fluido rho en kg/m³.
 * @property areaTransversal Área de sección transversal A en m².
 * @property calculadorAerodinamico Servicio inyectado para evaluar la fuerza y aceleración de arrastre.
 * @property motorFisica Motor de integración numérica inyectado para evolucionar el estado del proyectil.
 */
class Simulador(
    private val gravedadFoco: FuenteGravedad,
    private val coeficienteRestitucion: Double = DEFAULT_COEFICIENTE_RESTITUCION,
    private val coeficienteArrastre: Double = DEFAULT_COEFICIENTE_ARRASTRE,
    private val pasoTiempoMuestreo: Double = PASO_TIEMPO_MUESTREO,
    private val densidadAire: Double = DEFAULT_DENSIDAD_AIRE,
    private val areaTransversal: Double = DEFAULT_AREA_TRANSVERSAL,
    private val calculadorAerodinamico: CalculadorFuerzaAerodinamica = CalculadorArrastreAerodinamico(
        densidadAire = densidadAire,
        coeficienteArrastre = coeficienteArrastre,
        areaTransversal = areaTransversal
    ),
    private val motorFisica: MotorFisica2D = MotorFisicaEuler2D(calculadorAerodinamico)
) : CalculadorTrayectoria {

    fun obtenerCoeficienteRestitucion(): Double = coeficienteRestitucion
    fun obtenerCoeficienteArrastre(): Double = coeficienteArrastre
    fun obtenerPasoTiempoMuestreo(): Double = pasoTiempoMuestreo
    fun obtenerDensidadAire(): Double = densidadAire
    fun obtenerAreaTransversal(): Double = areaTransversal
    fun obtenerCalculadorAerodinamico(): CalculadorFuerzaAerodinamica = calculadorAerodinamico
    fun obtenerMotorFisica(): MotorFisica2D = motorFisica

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
        require(densidadAire >= 0.0) {
            "La densidad del aire debe ser mayor o igual a 0 (recibido: $densidadAire)."
        }
        require(areaTransversal >= 0.0) {
            "El área transversal debe ser mayor o igual a 0 (recibido: $areaTransversal)."
        }
    }

    /**
     * Delega el cálculo de la fuerza de arrastre aerodinámico en [calculadorAerodinamico].
     */
    internal fun calcularFuerzaArrastre(velocidadRelativa: Double): Double {
        return calculadorAerodinamico.calcularFuerzaArrastre(velocidadRelativa, 0.0)
    }

    /**
     * Calcula la trayectoria completa del proyectil manteniendo la aceleración del viento (ax)
     * de forma ininterrumpida durante el vuelo y todos los rebotes sucesivos.
     */
    override fun calcularTrayectoria(config: ConfigSimulacion): ResultadoSimulacion {
        if (config.velocidadInicial <= 0.0 || config.angulo <= 0.0) {
            return crearResultadoEnReposoInicial()
        }

        val estadoInicial = crearPuntoInicial(config)
        return ejecutarCicloSimulacion(config, estadoInicial)
    }

    /**
     * Sobrecarga de conveniencia que utiliza la configuración por defecto de masa y viento.
     */
    fun calcularTrayectoria(velocidadInicial: Double, anguloGrados: Double): ResultadoSimulacion {
        val config = ConfigSimulacion(
            velocidadInicial = velocidadInicial,
            angulo = anguloGrados,
            fuenteGravedad = gravedadFoco
        )
        return calcularTrayectoria(config)
    }

    private fun crearResultadoEnReposoInicial(): ResultadoSimulacion {
        val puntoInicial = PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 0.0)
        return ResultadoSimulacion(
            tiempoVuelo = 0.0,
            distanciaMaxima = 0.0,
            alturaMaxima = 0.0,
            trayectoria = listOf(puntoInicial),
            cantidadRebotes = 0,
            desviacionLateralMaxima = 0.0,
            alturasMaximasPorPique = emptyList(),
            metricasImpacto = MetricasImpacto.IMPACTO_NULO
        )
    }

    private fun crearPuntoInicial(config: ConfigSimulacion): PuntoTrayectoria {
        val anguloRad = Math.toRadians(config.angulo)
        val vx = if (abs(config.angulo - 90.0) < EPSILON) 0.0 else config.velocidadInicial * cos(anguloRad)
        val vy = config.velocidadInicial * sin(anguloRad)
        return PuntoTrayectoria(tiempo = 0.0, x = 0.0, y = 0.0, z = 0.0, vx = vx, vy = vy, vz = 0.0)
    }

    private fun ejecutarCicloSimulacion(
        config: ConfigSimulacion,
        estadoInicial: PuntoTrayectoria
    ): ResultadoSimulacion {
        val g = config.fuenteGravedad.gravedad
        val masa = config.masaKg
        val dirVientoRad = Math.toRadians(config.direccionVientoGrados)
        val vientoX = -config.velocidadVientoMS() * cos(dirVientoRad)
        val vientoZ = -config.velocidadVientoMS() * sin(dirVientoRad)

        val trayectoria = mutableListOf(estadoInicial)
        val alturasMaximasPorPique = mutableListOf<Double>()
        var estado = estadoInicial
        var cantidadRebotes = 0
        var alturaMaximaPiqueActual = 0.0
        var alturaMaximaGlobal = 0.0
        var metricasImpacto = MetricasImpacto.IMPACTO_NULO
        var tiempoActual = 0.0
        var enReposo = false

        val subPasos = max(1, (pasoTiempoMuestreo / PASO_SUB_INTEGRACION).toInt())
        val dt = pasoTiempoMuestreo / subPasos

        while (!enReposo && tiempoActual < MAX_TIEMPO_SIMULACION) {
            for (paso in 0 until subPasos) {
                estado = motorFisica.avanzarEstado(estado, masa, g, vientoX, dt, vientoZ)
                alturaMaximaPiqueActual = max(alturaMaximaPiqueActual, estado.y)
                alturaMaximaGlobal = max(alturaMaximaGlobal, estado.y)

                if (estado.y <= 0.0 && estado.vy < 0.0) {
                    metricasImpacto = MetricasImpacto(vx = estado.vx, vy = estado.vy)
                    if (alturaMaximaPiqueActual > UMBRAL_ALTURA_PIQUE) {
                        alturasMaximasPorPique.add(alturaMaximaPiqueActual)
                    }
                    alturaMaximaPiqueActual = 0.0

                    if (tieneEnergiaParaRebotar(estado.vy)) {
                        estado = estado.copy(y = 0.0, vy = -estado.vy * coeficienteRestitucion)
                        cantidadRebotes++
                        registrarPuntoReboteIntermedio(trayectoria, estado, tiempoActual, paso, subPasos, dt)
                    } else {
                        estado = estado.copy(y = 0.0, vx = 0.0, vy = 0.0, vz = 0.0)
                        enReposo = true
                        break
                    }
                } else if (estado.y <= 0.0) {
                    estado = estado.copy(y = 0.0)
                }
            }

            tiempoActual += pasoTiempoMuestreo
            val tiempoMuestreado = redondearTiempo(tiempoActual)
            trayectoria.add(estado.copy(tiempo = tiempoMuestreado, y = max(0.0, estado.y)))
        }

        if (alturasMaximasPorPique.isEmpty() && alturaMaximaGlobal > 0.0) {
            alturasMaximasPorPique.add(alturaMaximaGlobal)
        }

        return ResultadoSimulacion(
            tiempoVuelo = trayectoria.last().tiempo,
            distanciaMaxima = trayectoria.last().x,
            alturaMaxima = alturaMaximaGlobal,
            trayectoria = trayectoria,
            cantidadRebotes = cantidadRebotes,
            desviacionLateralMaxima = trayectoria.maxOf { abs(it.z) },
            alturasMaximasPorPique = alturasMaximasPorPique,
            metricasImpacto = metricasImpacto
        )
    }

    private fun tieneEnergiaParaRebotar(vy: Double): Boolean {
        return abs(vy) > UMBRAL_VELOCIDAD_REBOTE && coeficienteRestitucion > 0.0
    }

    private fun registrarPuntoReboteIntermedio(
        trayectoria: MutableList<PuntoTrayectoria>,
        estadoRebote: PuntoTrayectoria,
        tiempoBase: Double,
        paso: Int,
        subPasos: Int,
        dt: Double
    ) {
        if (paso < subPasos - 1) {
            val tiempoRebote = redondearTiempo(tiempoBase + (paso + 1) * dt)
            trayectoria.add(estadoRebote.copy(tiempo = tiempoRebote, y = 0.0))
        }
    }

    private fun redondearTiempo(tiempo: Double): Double = round(tiempo * 1000.0) / 1000.0

    companion object {
        const val PASO_TIEMPO_MUESTREO: Double = 0.1
        const val DEFAULT_COEFICIENTE_RESTITUCION: Double = 0.75
        const val DEFAULT_COEFICIENTE_ARRASTRE: Double = 0.05
        const val DEFAULT_DENSIDAD_AIRE: Double = 1.225
        const val DEFAULT_AREA_TRANSVERSAL: Double = 0.5
        const val UMBRAL_VELOCIDAD_REBOTE: Double = 0.2
        const val UMBRAL_ALTURA_PIQUE: Double = 1e-4
        private const val PASO_SUB_INTEGRACION: Double = 0.001
        private const val MAX_TIEMPO_SIMULACION: Double = 300.0
        private const val EPSILON: Double = 1e-9
    }
}
