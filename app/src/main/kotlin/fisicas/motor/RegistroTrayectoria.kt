package fisicas.motor

/**
 * Contrato de abstracción encargado de acumular los puntos muestreados y las métricas
 * de piques y rebotes durante la integración cinemática para construir [ResultadoSimulacion].
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Aísla la recolección de la serie temporal y
 *   el cálculo de métricas de impacto respecto del bucle de integración y de la cinemática.
 * - Interface Segregation Principle (ISP): Expone únicamente las tres operaciones necesarias.
 */
interface RegistroTrayectoria {

    /**
     * Registra la altura actual y el resultado del contacto contra el suelo en un sub-paso
     * (`0` en el aire, `1` rebote, `-1` reposo).
     */
    fun registrarSubPaso(alturaActual: Double, codigoContacto: Int)

    /**
     * Agrega un punto cinemático muestreado a la trayectoria acumulada.
     */
    fun registrarPunto(punto: PuntoTrayectoria)

    /**
     * Construye el [ResultadoSimulacion] inmutable con todas las muestras y métricas registradas.
     */
    fun construirResultado(): ResultadoSimulacion
}
