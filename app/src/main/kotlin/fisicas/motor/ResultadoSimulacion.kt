package fisicas.motor

/**
 * Empaqueta de forma inmutable los resultados calculados por el motor físico.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Actúa como modelo de resultados inmutable
 *   para transportar las métricas globales y la serie temporal tridimensional de la trayectoria.
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
) {
    /**
     * Indica si el proyectil registró al menos un rebote efectivo durante el vuelo.
     */
    fun tieneRebotes(): Boolean = cantidadRebotes > 0 && tiempoVuelo > 0.0
}
