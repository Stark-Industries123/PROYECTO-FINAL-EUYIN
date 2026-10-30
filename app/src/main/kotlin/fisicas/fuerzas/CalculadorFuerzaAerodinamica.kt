package fisicas.fuerzas

/**
 * Contrato de abstracción para el cálculo de la fuerza de arrastre aerodinámico
 * y la aceleración resultante según la masa del proyectil.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Define únicamente el cálculo aerodinámico por eje.
 * - Dependency Inversion Principle (DIP): Permite que el motor físico dependa de esta abstracción
 *   en lugar de una fórmula concreta de resistencia fluida.
 */
interface CalculadorFuerzaAerodinamica {

    /**
     * Calcula la fuerza de arrastre aerodinámico actuante sobre el proyectil en un eje:
     * Fx = 0.5 * rho * (v_viento - vx)^2 * Cd * A, conservando el sentido vectorial
     * de la velocidad relativa (v_viento - vx).
     *
     * @param velocidadViento Componente de velocidad del viento en el eje (m/s).
     * @param velocidadProyectil Componente de velocidad instantánea del proyectil en el eje (m/s).
     * @return Fuerza de arrastre resultante en Newtons (N).
     */
    fun calcularFuerzaArrastre(velocidadViento: Double, velocidadProyectil: Double): Double

    /**
     * Determina la aceleración producida por el arrastre aerodinámico aplicando
     * la Segunda Ley de Newton: ax = Fx / m.
     *
     * @param velocidadViento Componente de velocidad del viento en el eje (m/s).
     * @param velocidadProyectil Componente de velocidad instantánea del proyectil en el eje (m/s).
     * @param masa Masa del proyectil en kilogramos (debe ser > 0).
     * @return Aceleración aerodinámica en m/s².
     */
    fun calcularAceleracion(velocidadViento: Double, velocidadProyectil: Double, masa: Double): Double
}
