package fisicas.estado

import fisicas.vector.Vector2D

/**
 * Representa el estado cinemático instantáneo del proyectil en el espacio bidimensional.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Modela únicamente la cinemática temporal (posición, velocidad, tiempo).
 * - Inmutabilidad (POO): Estructura inmutable; las transiciones temporales producen nuevas instancias.
 * - Anti-patrón Evitado: No es una clase anémica, provee consultas de estado de alto nivel y transiciones puras.
 *
 * @property posicion Posición espacial (x, y) en metros.
 * @property velocidad Vector de velocidad instantánea (vx, vy) en m/s.
 * @property tiempo Tiempo transcurrido en segundos desde el inicio.
 * @property estaEnReposo Indica si el cuerpo ha detenido su movimiento.
 */
data class ProjectileState(
    val posicion: Vector2D,
    val velocidad: Vector2D,
    val tiempo: Double = 0.0,
    val estaEnReposo: Boolean = false
) {
    /**
     * Rapidez escalar instantánea: ||v|| = sqrt(vx^2 + vy^2).
     */
    val rapidez: Double get() = velocidad.magnitud()

    /**
     * Indica si el cuerpo ha alcanzado o traspasado el límite del suelo (Y <= 0).
     */
    val estaEnSuelo: Boolean get() = posicion.y <= 0.0

    /**
     * Genera una nueva instancia reflejando la evolución temporal.
     */
    fun avanzar(nuevaPosicion: Vector2D, nuevaVelocidad: Vector2D, deltaTiempo: Double): ProjectileState {
        return copy(
            posicion = nuevaPosicion,
            velocidad = nuevaVelocidad,
            tiempo = tiempo + deltaTiempo
        )
    }

    /**
     * Genera un nuevo estado en reposo forzando velocidad cero y posicionado sobre el plano del suelo.
     */
    fun detener(tiempoDetencion: Double = tiempo): ProjectileState {
        return copy(
            posicion = Vector2D(posicion.x, 0.0),
            velocidad = Vector2D.CERO,
            tiempo = tiempoDetencion,
            estaEnReposo = true
        )
    }

    /**
     * Retorna una copia con el vector de velocidad modificado.
     */
    fun conVelocidad(nuevaVelocidad: Vector2D): ProjectileState = copy(velocidad = nuevaVelocidad)

    /**
     * Retorna una copia con la posición modificada.
     */
    fun conPosicion(nuevaPosicion: Vector2D): ProjectileState = copy(posicion = nuevaPosicion)

    companion object {
        val ORIGEN_REPOSO = ProjectileState(
            posicion = Vector2D.CERO,
            velocidad = Vector2D.CERO,
            tiempo = 0.0,
            estaEnReposo = true
        )
    }
}
