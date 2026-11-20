package fisicas.motor

import config.ConfigSimulacion

/**
 * Contrato de abstracción encargado de construir el estado cinemático inicial
 * de una simulación a partir de su configuración física y parámetros de integración.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Dependency Inversion Principle (DIP): Permite que [IntegradorCinematica3D] dependa
 *   únicamente de abstracciones en lugar de instanciar implementaciones concretas.
 * - Interface Segregation Principle (ISP): Expone un contrato mínimo de una única operación.
 */
interface FabricaEstadoCinematica {

    /**
     * Crea una instancia de [EstadoCinematica] preparada para simular [config] bajo [parametros].
     */
    fun crear(config: ConfigSimulacion, parametros: ParametrosIntegracion): EstadoCinematica
}
