package fisicas.motor

/**
 * Representa un punto discreto en el tiempo y el espacio tridimensional (3D) dentro de la
 * trayectoria del proyectil, incluyendo componentes cinemáticas instantáneas.
 *
 * @property tiempo Tiempo transcurrido en segundos desde el inicio del lanzamiento.
 * @property x Posición horizontal frontal alcanzada en metros (eje de avance del proyectil).
 * @property y Posición vertical (altura) alcanzada en metros (referenciada al suelo Y = 0).
 * @property z Posición lateral alcanzada en metros (desviación lateral por efecto del viento).
 * @property vx Componente de velocidad instantánea en el eje frontal X (m/s).
 * @property vy Componente de velocidad instantánea en el eje vertical Y (m/s).
 * @property vz Componente de velocidad instantánea en el eje lateral Z (m/s).
 */
data class PuntoTrayectoria(
    val tiempo: Double,
    val x: Double,
    val y: Double,
    val z: Double = 0.0,
    val vx: Double = 0.0,
    val vy: Double = 0.0,
    val vz: Double = 0.0
) {
    /**
     * Alias de conveniencia para acceder al tiempo transcurrido `t` en segundos.
     */
    val t: Double get() = tiempo

    /**
     * Indica si el punto se encuentra apoyado sobre la cota del suelo (Y <= 0).
     */
    fun estaEnSuelo(): Boolean = y <= 0.0 && tiempo >= 0.0
}
