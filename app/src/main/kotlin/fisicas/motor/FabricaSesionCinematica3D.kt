package fisicas.motor

import config.ConfigSimulacion

/**
 * Fábrica concreta encargada de validar el coeficiente de restitución e instanciar
 * sesiones tridimensionales [SesionCinematica3D] respaldadas por [EstadoCinematica3D].
 */
class FabricaSesionCinematica3D(
    private val coeficienteRestitucion: Double = 0.75
) : FabricaSesionCinematica {

    init {
        require(coeficienteRestitucion in 0.0..1.0) {
            "El coeficiente de restitución debe estar entre 0.0 y 1.0 (recibido: $coeficienteRestitucion)."
        }
    }

    override fun crearSesion(config: ConfigSimulacion, dt: Double): SesionCinematica {
        val estado = EstadoCinematica3D(config.velocidadInicial, config.angulo, dt, coeficienteRestitucion)
        return SesionCinematica3D(estado)
    }
}
