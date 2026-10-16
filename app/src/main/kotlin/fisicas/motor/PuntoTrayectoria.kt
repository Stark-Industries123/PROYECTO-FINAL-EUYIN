package fisicas.motor

/**
 * Representa un punto discreto en el tiempo y espacio dentro de la trayectoria del proyectil.
 *
 * @property tiempo Tiempo transcurrido en segundos desde el inicio del lanzamiento.
 * @property x Posición horizontal alcanzada en metros.
 * @property y Posición vertical (altura) alcanzada en metros.
 */
data class PuntoTrayectoria(
    val tiempo: Double,
    val x: Double,
    val y: Double
)
