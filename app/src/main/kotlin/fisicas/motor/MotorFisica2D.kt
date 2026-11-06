package fisicas.motor

/**
 * Contrato de abstracción para el motor de integración numérica 2D/3D del estado del proyectil.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Aísla la integración matemática de un paso diferencial
 *   de tiempo dt respecto del bucle de simulación y de la interfaz de consola.
 * - Dependency Inversion Principle (DIP): Permite desacoplar el simulador del esquema numérico
 *   específico (Euler-Cromer, Verlet, Runge-Kutta).
 */
interface MotorFisica2D {

    /**
     * Integra numéricamente las ecuaciones cinemáticas durante un paso temporal [dt]
     * a partir de las componentes de aceleración instantánea.
     *
     * @param estado Estado cinemático actual del proyectil.
     * @param ax Aceleración horizontal en el eje X (m/s²).
     * @param ay Aceleración vertical en el eje Y (m/s²).
     * @param dt Paso diferencial de tiempo en segundos (> 0).
     * @param az Aceleración lateral en el eje Z (m/s², por defecto 0.0).
     * @return Nuevo [PuntoTrayectoria] con velocidades y posiciones actualizadas.
     */
    fun integrarPaso(
        estado: PuntoTrayectoria,
        ax: Double,
        ay: Double,
        dt: Double,
        az: Double = 0.0
    ): PuntoTrayectoria

    /**
     * Calcula las aceleraciones aerodinámicas (ax, az) y gravitatoria-aerodinámica (ay)
     * en función de la masa y el viento, e integra el estado durante un diferencial [dt].
     *
     * @param estado Estado cinemático actual del proyectil.
     * @param masa Masa del proyectil en kilogramos (> 0).
     * @param gravedad Aceleración gravitatoria del entorno en m/s².
     * @param vientoX Componente de velocidad del viento en el eje X (m/s).
     * @param dt Paso diferencial de tiempo en segundos (> 0).
     * @param vientoZ Componente de velocidad del viento en el eje Z (m/s, por defecto 0.0).
     * @return Nuevo [PuntoTrayectoria] evolucionado en el tiempo.
     */
    fun avanzarEstado(
        estado: PuntoTrayectoria,
        masa: Double,
        gravedad: Double,
        vientoX: Double,
        dt: Double,
        vientoZ: Double = 0.0
    ): PuntoTrayectoria
}
