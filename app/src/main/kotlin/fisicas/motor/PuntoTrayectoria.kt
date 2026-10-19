package fisicas.motor

/**
 * Representa un punto discreto en el tiempo y el espacio tridimensional (3D) dentro de la
 * trayectoria del proyectil, incluyendo componentes cinemáticas instantáneas.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Modela exclusivamente el estado cinemático tridimensional
 *   del proyectil en un instante de tiempo dado.
 * - Open/Closed Principle (OCP): Abierto a soportar futuras proyecciones gráficas 2D (lateral X-Y,
 *   superior X-Z) y representaciones tridimensionales completas (X-Y-Z) sin romper el código existente.
 * - Inmutabilidad (POO): Todas las propiedades son de solo lectura (`val`), garantizando thread-safety
 *   y ausencia de efectos secundarios.
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
}
