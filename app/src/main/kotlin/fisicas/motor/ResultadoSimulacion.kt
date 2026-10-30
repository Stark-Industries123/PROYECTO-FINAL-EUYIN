package fisicas.motor

/**
 * Empaqueta de forma inmutable los resultados globales y la serie temporal calculados por el motor físico.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Transporta las métricas consolidadas, la colección de
 *   puntos de la trayectoria y el Value Object [MetricasImpacto].
 * - Open/Closed Principle (OCP): Abierto a extensión mediante propiedades con valores por defecto
 *   sin romper contratos de código preexistente.
 *
 * @property tiempoVuelo Tiempo total transcurrido en segundos hasta el reposo del proyectil.
 * @property distanciaMaxima Alcance frontal final alcanzado en el eje X en metros (puede ser < 0 con viento fuerte en contra).
 * @property alturaMaxima Altura máxima alcanzada en el eje vertical Y en metros.
 * @property trayectoria Lista inmutable de puntos cinemáticos muestreados en el tiempo.
 * @property cantidadRebotes Número de impactos con rebote efectivo contra el suelo (Y = 0).
 * @property desviacionLateralMaxima Magnitud máxima de desviación en el eje lateral Z en metros.
 * @property alturasMaximasPorPique Alturas máximas registradas en cada arco o pique de la trayectoria.
 * @property metricasImpacto Objeto de valor inmutable con la velocidad escalar final y el ángulo de llegada al suelo.
 */
data class ResultadoSimulacion(
    val tiempoVuelo: Double,
    val distanciaMaxima: Double,
    val alturaMaxima: Double,
    val trayectoria: List<PuntoTrayectoria> = emptyList(),
    val cantidadRebotes: Int = 0,
    val desviacionLateralMaxima: Double = 0.0,
    val alturasMaximasPorPique: List<Double> = emptyList(),
    val metricasImpacto: MetricasImpacto = MetricasImpacto.IMPACTO_NULO
) {
    /**
     * Indica si el proyectil experimentó desplazamiento hacia atrás (X < 0) en algún punto o al final.
     */
    fun tieneDesplazamientoNegativo(): Boolean {
        return distanciaMaxima < 0.0 || trayectoria.any { it.x < 0.0 }
    }

    /**
     * Indica si se produjeron rebotes efectivos contra el suelo durante la simulación.
     */
    fun huboRebotes(): Boolean = cantidadRebotes > 0

    /**
     * Retorna el último punto registrado en la trayectoria, o null si está vacía.
     */
    fun obtenerPuntoFinal(): PuntoTrayectoria? = trayectoria.lastOrNull()
}
