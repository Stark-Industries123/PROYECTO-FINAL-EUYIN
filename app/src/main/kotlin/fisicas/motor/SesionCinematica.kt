package fisicas.motor

/**
 * Contrato que encapsula el estado y la recolección de muestras de una simulación cinemática 3D.
 *
 * Cumplimiento de SOLID:
 * - Interface Segregation Principle (ISP): Expone únicamente las 3 operaciones necesarias para
 *   avanzar el paso diferencial, registrar muestras temporales y construir el resultado final.
 */
interface SesionCinematica {

    /**
     * Avanza un sub-paso diferencial evaluando aceleraciones e impactos contra el suelo.
     * Retorna `true` cuando el proyectil alcanza el estado de reposo definitivo.
     */
    fun avanzarSubPaso(gravedad: Double, factorArrastre: Double, vientoX: Double, vientoZ: Double): Boolean

    /**
     * Captura y almacena el estado cinemático actual en el instante [tiempo].
     */
    fun capturarMuestra(tiempo: Double)

    /**
     * Construye el [ResultadoSimulacion] inmutable con las métricas y trayectoria recolectadas.
     */
    fun construirResultado(): ResultadoSimulacion
}
