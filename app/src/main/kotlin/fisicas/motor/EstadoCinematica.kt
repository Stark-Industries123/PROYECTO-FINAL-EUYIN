package fisicas.motor

/**
 * Contrato de abstracción para el estado cinemático diferencial durante la integración numérica.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Interface Segregation Principle (ISP): Expone únicamente las tres operaciones de alto
 *   nivel requeridas por el bucle de integración temporal.
 */
interface EstadoCinematica {

    /**
     * Evalúa las aceleraciones aerodinámica y gravitatoria, integra un diferencial de tiempo
     * y evalúa el contacto contra el suelo.
     *
     * @return `true` si el proyectil alcanzó el estado de reposo definitivo.
     */
    fun avanzarSubPaso(): Boolean

    /**
     * Registra una muestra instantánea del estado cinemático para el instante [tiempo] en segundos.
     */
    fun registrarMuestra(tiempo: Double)

    /**
     * Construye el [ResultadoSimulacion] inmutable con las muestras y métricas acumuladas.
     */
    fun construirResultado(): ResultadoSimulacion
}
