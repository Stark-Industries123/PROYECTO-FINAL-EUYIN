package fisicas.motor

/**
 * Contrato de abstracción para el estado cinemático diferencial durante la integración numérica.
 */
interface EstadoCinematica {

    /**
     * Evalúa las aceleraciones aerodinámica y gravitatoria e integra un diferencial de tiempo.
     */
    fun avanzarSubPaso(g: Double, factorArrastre: Double, vientoX: Double, vientoZ: Double): Double

    /**
     * Registra las alturas máximas y evalúa el contacto con el suelo.
     */
    fun registrarImpacto(piques: MutableList<Double>, metricas: DoubleArray): Boolean

    /**
     * Verifica si el proyectil tocó el suelo (Y <= 0) con velocidad descendente.
     */
    fun evaluarContactoSuelo(): Int

    /**
     * Construye una instantánea inmutable [PuntoTrayectoria] para el instante indicado.
     */
    fun crearPunto(tiempo: Double): PuntoTrayectoria
}
