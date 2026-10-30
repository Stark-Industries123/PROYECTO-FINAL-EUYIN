package fisicas.motor

import config.ConfigSimulacion
import fisicas.gravedad.FuenteGravedad
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.round
import kotlin.math.sin

/**
 * Empaqueta de forma inmutable los resultados calculados por el motor físico.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Actúa como Data Transfer Object (DTO) inmutable
 *   para transportar las métricas globales y la serie temporal tridimensional de la trayectoria.
 * - Open/Closed Principle (OCP): Abierto a extensión mediante propiedades con valores por defecto
 *   sin romper contratos de código preexistente.
 *
 * @property tiempoVuelo Tiempo total transcurrido en segundos hasta el reposo del proyectil.
 * @property distanciaMaxima Alcance frontal máximo alcanzado en el eje X en metros.
 * @property alturaMaxima Altura máxima alcanzada en el eje vertical Y en metros.
 * @property trayectoria Lista inmutable de puntos cinemáticos tridimensionales muestreados en el tiempo.
 * @property cantidadRebotes Número de impactos con rebote efectivo contra el suelo (Y = 0).
 * @property desviacionLateralMaxima Magnitud máxima de desviación en el eje lateral Z en metros.
 * @property alturasMaximasPorPique Alturas máximas registradas en cada arco o pique de la trayectoria.
 */
data class ResultadoSimulacion(
    val tiempoVuelo: Double,
    val distanciaMaxima: Double,
    val alturaMaxima: Double,
    val trayectoria: List<PuntoTrayectoria> = emptyList(),
    val cantidadRebotes: Int = 0,
    val desviacionLateralMaxima: Double = 0.0,
    val alturasMaximasPorPique: List<Double> = emptyList()
)

/**
 * Motor físico encargado de simular la dinámica tridimensional del tiro de un proyectil
 * incorporando gravedad, modelo de arrastre aerodinámico y viento 360°, y mecánica de
 * rebote contra el suelo.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Su única responsabilidad es ejecutar la integración
 *   física y computar la cinemática del proyectil. No realiza validaciones de entrada de consola
 *   ni renderizado de caracteres.
 * - Dependency Inversion Principle (DIP): Implementa la abstracción [CalculadorTrayectoria] y depende
 *   de la interfaz [FuenteGravedad], desacoplando los cálculos de cuerpos celestes específicos.
 * - Open/Closed Principle (OCP): Extensible mediante inyección de coeficientes de restitución y
 *   arrastre, y extensible a futuras vistas 2D (frontal, superior) y 3D completas a través de [PuntoTrayectoria].
 *
 * @property gravedadFoco Abstracción que provee la aceleración gravitacional del entorno.
 * @property coeficienteRestitucion Fracción de velocidad vertical preservada tras impactar el suelo (0.0 a 1.0).
 * @property coeficienteArrastre Constante de proporcionalidad para el modelo de resistencia aerodinámica.
 * @property pasoTiempoMuestreo Intervalo temporal en segundos entre puntos sucesivos registrados en la trayectoria.
 */
class Simulador(
    private val gravedadFoco: FuenteGravedad,
    private val coeficienteRestitucion: Double = DEFAULT_COEFICIENTE_RESTITUCION,
    private val coeficienteArrastre: Double = DEFAULT_COEFICIENTE_ARRASTRE,
    private val pasoTiempoMuestreo: Double = PASO_TIEMPO_MUESTREO,
    private val densidadAire: Double = DEFAULT_DENSIDAD_AIRE,
    private val areaTransversal: Double = DEFAULT_AREA_TRANSVERSAL
) : CalculadorTrayectoria {

    fun obtenerCoeficienteRestitucion(): Double = coeficienteRestitucion
    fun obtenerCoeficienteArrastre(): Double = coeficienteArrastre
    fun obtenerPasoTiempoMuestreo(): Double = pasoTiempoMuestreo
    fun obtenerDensidadAire(): Double = densidadAire
    fun obtenerAreaTransversal(): Double = areaTransversal

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
     * Calcula la fuerza de arrastre aerodinámico cuadrático en un eje según la ecuación:
     * F_d = (1/2) * rho * v_relativa^2 * C_d * A, preservando el sentido del flujo relativo.
     *
     * @param velocidadRelativa Velocidad relativa del fluido respecto del proyectil (v_viento - v_proyectil).
     * @return Fuerza de arrastre resultante en Newtons (N).
     */
    internal fun calcularFuerzaArrastre(velocidadRelativa: Double): Double {
        return 0.5 * densidadAire * velocidadRelativa * abs(velocidadRelativa) * coeficienteArrastre * areaTransversal
    }

    /**
     * Calcula la trayectoria completa del proyectil a partir del objeto inmutable [ConfigSimulacion].
     *
     * Integra numéricamente las ecuaciones diferenciales de movimiento:
     * - Gravedad constante hacia el suelo en Y: a_g = -g.
     * - Descomposición vectorial del viento en ejes X (frontal/trasero) y Z (lateral).
     * - Fuerza de arrastre proporcional a la velocidad relativa respecto del viento: F = -k * v_rel.
     * - Aceleración eólica e inercial inversamente dependiente de la masa: a_drag = F / m = -(k / m) * v_rel.
     * - Mecánica de rebotes en el suelo Y = 0 con inversión de velocidad vertical v_y = -v_y * e hasta reposo.
     *
     * @param config Configuración inmutable de disparo con parámetros de masa, viento y gravedad.
     * @return [ResultadoSimulacion] con la métrica física y los puntos 3D de la trayectoria.
     */
    override fun calcularTrayectoria(config: ConfigSimulacion): ResultadoSimulacion {
        val g = config.fuenteGravedad.gravedad

        // Casos borde: velocidad inicial 0 o ángulo de 0° (en el suelo sin componente vertical)
        if (config.velocidadInicial <= 0.0 || config.angulo <= 0.0) {
            val puntoInicial = PuntoTrayectoria(
                tiempo = 0.0,
                x = 0.0,
                y = 0.0,
                z = 0.0,
                vx = 0.0,
                vy = 0.0,
                vz = 0.0
            )
            return ResultadoSimulacion(
                tiempoVuelo = 0.0,
                distanciaMaxima = 0.0,
                alturaMaxima = 0.0,
                trayectoria = listOf(puntoInicial),
                cantidadRebotes = 0,
                desviacionLateralMaxima = 0.0,
                alturasMaximasPorPique = emptyList()
            )
        }

        val anguloRad = Math.toRadians(config.angulo)

        // Descomposición inicial de velocidad (tiro vertical puro a 90° anula v_x para evitar error de coma flotante)
        var vx = if (abs(config.angulo - 90.0) < EPSILON) 0.0 else config.velocidadInicial * cos(anguloRad)
        var vy = config.velocidadInicial * sin(anguloRad)
        var vz = 0.0

        var x = 0.0
        var y = 0.0
        var z = 0.0
        var tiempoActual = 0.0

        val masa = config.masaKg
        val vVientoMS = config.velocidadVientoMS()
        val dirVientoRad = Math.toRadians(config.direccionVientoGrados)

        // Descomposición vectorial del viento (0° = Norte / Viento frontal en contra, 90° = Este / Lateral en Z)
        val vientoX = -vVientoMS * cos(dirVientoRad)
        val vientoZ = -vVientoMS * sin(dirVientoRad)

        val trayectoria = mutableListOf<PuntoTrayectoria>()
        trayectoria.add(
            PuntoTrayectoria(
                tiempo = 0.0,
                x = 0.0,
                y = 0.0,
                z = 0.0,
                vx = vx,
                vy = vy,
                vz = vz
            )
        )

        var cantidadRebotes = 0
        val alturasMaximasPorPique = mutableListOf<Double>()
        var alturaMaximaPiqueActual = 0.0
        var alturaMaximaGlobal = 0.0
        var enReposo = false

        val subPasos = max(1, (pasoTiempoMuestreo / PASO_SUB_INTEGRACION).toInt())
        val dt = pasoTiempoMuestreo / subPasos

        while (!enReposo && tiempoActual < MAX_TIEMPO_SIMULACION) {
            for (paso in 0 until subPasos) {
                // Velocidad relativa del viento respecto del proyectil (v_rel = v_viento - v_proyectil)
                val vrelX = vientoX - vx
                val vrelY = -vy
                val vrelZ = vientoZ - vz

                // Fuerza de arrastre aerodinámico cuadrático: F_d = (1/2) * rho * v_relativa^2 * C_d * A
                val fdX = calcularFuerzaArrastre(vrelX)
                val fdY = calcularFuerzaArrastre(vrelY)
                val fdZ = calcularFuerzaArrastre(vrelZ)

                // Aceleraciones por Segunda Ley de Newton: a = F_d / m (más gravedad en Y)
                val ax = fdX / masa
                val ay = -g + (fdY / masa)
                val az = fdZ / masa

                // Integración numérica (Euler-Cromer)
                vx += ax * dt
                vy += ay * dt
                vz += az * dt

                x += vx * dt
                y += vy * dt
                z += vz * dt

                if (y > alturaMaximaPiqueActual) {
                    alturaMaximaPiqueActual = y
                }
                if (y > alturaMaximaGlobal) {
                    alturaMaximaGlobal = y
                }

                // Detección de impacto contra el suelo (Y <= 0)
                if (y <= 0.0) {
                    y = 0.0
                    if (vy < 0.0) {
                        if (abs(vy) > UMBRAL_VELOCIDAD_REBOTE && coeficienteRestitucion > 0.0) {
                            // Rebote significativo: invertimos la velocidad vertical amortiguada
                            if (alturaMaximaPiqueActual > UMBRAL_ALTURA_PIQUE) {
                                alturasMaximasPorPique.add(alturaMaximaPiqueActual)
                            }
                            alturaMaximaPiqueActual = 0.0
                            vy = -vy * coeficienteRestitucion
                            cantidadRebotes++
                            if (paso < subPasos - 1) {
                                val tiempoRebote = round((tiempoActual + (paso + 1) * dt) * 1000.0) / 1000.0
                                trayectoria.add(
                                    PuntoTrayectoria(
                                        tiempo = tiempoRebote,
                                        x = x,
                                        y = 0.0,
                                        z = z,
                                        vx = vx,
                                        vy = vy,
                                        vz = vz
                                    )
                                )
                            }
                        } else {
                            // Energía de rebote despreciable: fin del movimiento (reposo)
                            if (alturaMaximaPiqueActual > UMBRAL_ALTURA_PIQUE) {
                                alturasMaximasPorPique.add(alturaMaximaPiqueActual)
                            }
                            alturaMaximaPiqueActual = 0.0
                            vy = 0.0
                            vx = 0.0
                            vz = 0.0
                            enReposo = true
                            break
                        }
                    }
                }
            }

            tiempoActual += pasoTiempoMuestreo
            val tiempoMuestreado = round(tiempoActual * 1000.0) / 1000.0

            trayectoria.add(
                PuntoTrayectoria(
                    tiempo = tiempoMuestreado,
                    x = x,
                    y = max(0.0, y),
                    z = z,
                    vx = vx,
                    vy = vy,
                    vz = vz
                )
            )

            if (enReposo) {
                break
            }
        }

        // Si no hubo piques previos pero sí vuelo inicial
        if (alturasMaximasPorPique.isEmpty() && alturaMaximaGlobal > 0.0) {
            alturasMaximasPorPique.add(alturaMaximaGlobal)
        }

        val tiempoVuelo = trayectoria.last().tiempo
        val distanciaMaxima = trayectoria.last().x
        val desviacionLateralMaxima = trayectoria.maxOf { abs(it.z) }

        return ResultadoSimulacion(
            tiempoVuelo = tiempoVuelo,
            distanciaMaxima = distanciaMaxima,
            alturaMaxima = alturaMaximaGlobal,
            trayectoria = trayectoria,
            cantidadRebotes = cantidadRebotes,
            desviacionLateralMaxima = desviacionLateralMaxima,
            alturasMaximasPorPique = alturasMaximasPorPique
        )
    }

    /**
     * Sobrecarga de conveniencia que mantiene compatibilidad retrocompatible con versiones previas,
     * delegando en [calcularTrayectoria] con la configuración por defecto de masa y viento.
     *
     * @param velocidadInicial Velocidad inicial de disparo en m/s.
     * @param anguloGrados Ángulo de elevación en grados.
     * @return [ResultadoSimulacion] calculado.
     */
    fun calcularTrayectoria(velocidadInicial: Double, anguloGrados: Double): ResultadoSimulacion {
        val config = ConfigSimulacion(
            velocidadInicial = velocidadInicial,
            angulo = anguloGrados,
            fuenteGravedad = gravedadFoco
        )
        return calcularTrayectoria(config)
    }

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
