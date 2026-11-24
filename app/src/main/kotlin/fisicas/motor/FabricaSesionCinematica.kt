package fisicas.motor

import config.ConfigSimulacion

/**
 * Fábrica abstracta encargada de crear una [SesionCinematica] inicializada para una
 * configuración de simulación y un diferencial de tiempo dados.
 *
 * Cumplimiento de SOLID:
 * - Dependency Inversion Principle (DIP): Desacopla a [IntegradorCinematica3D] de las clases
 *   concretas de estado y acumulación cinemática.
 */
interface FabricaSesionCinematica {

    /**
     * Crea una nueva [SesionCinematica] a partir de [config] y el paso diferencial [dt].
     */
    fun crearSesion(config: ConfigSimulacion, dt: Double): SesionCinematica
}
