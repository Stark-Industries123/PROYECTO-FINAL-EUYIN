package condiciones.servicio

import condiciones.dominio.CondicionesIniciales
import condiciones.dominio.Proyectil
import condiciones.dominio.Viento

/**
 * Contrato de abstracción para el servicio ensamblador de condiciones iniciales de dominio.
 */
interface EnsambladorCondiciones {

    /**
     * Calcula el rumbo náutico exacto del viento y ensambla [CondicionesIniciales].
     */
    fun ensamblar(
        direccionGrados: Double,
        velocidadViento: Double,
        pesoProyectil: Double
    ): CondicionesIniciales

    /**
     * Construye la entidad [Viento] asociando el rumbo calculado.
     */
    fun crearViento(direccionGrados: Double, velocidadViento: Double): Viento

    /**
     * Construye la entidad [Proyectil].
     */
    fun crearProyectil(pesoProyectil: Double): Proyectil
}
