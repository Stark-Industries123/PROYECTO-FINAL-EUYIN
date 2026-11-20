package fisicas.motor

import config.ConfigSimulacion

/**
 * Implementación concreta de [FabricaEstadoCinematica] que instancia [EstadoCinematica3D]
 * junto con su acumulador de trayectoria [RegistroTrayectoria3D].
 *
 * Cumplimiento de principios POO y SOLID:
 * - Single Responsibility Principle (SRP): Encapsula únicamente la creación e inicialización
 *   del estado cinemático 3D y su registro de trayectoria asociado.
 * - Open/Closed Principle (OCP): Permite reemplazar la implementación del estado o del
 *   registro sin modificar [IntegradorCinematica3D].
 */
class FabricaEstadoCinematica3D : FabricaEstadoCinematica {

    override fun crear(
        config: ConfigSimulacion,
        parametros: ParametrosIntegracion
    ): EstadoCinematica {
        return EstadoCinematica3D(
            config = config,
            parametros = parametros,
            registro = RegistroTrayectoria3D()
        )
    }
}
