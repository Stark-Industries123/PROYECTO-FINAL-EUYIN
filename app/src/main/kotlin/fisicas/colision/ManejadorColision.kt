package fisicas.colision

import fisicas.config.BounceConfig
import fisicas.estado.ProjectileState

/**
 * Contrato de abstracción para la detección y resolución de colisiones del proyectil.
 *
 * Cumplimiento de principios POO y SOLID:
 * - Dependency Inversion Principle (DIP): Desacopla la lógica de colisión del motor físico principal.
 * - Single Responsibility Principle (SRP): Su única responsabilidad es procesar las interacciones de contacto.
 */
interface ManejadorColision {

    /**
     * Evalúa si el proyectil ha impactado contra una superficie y aplica las leyes de restitución y fricción.
     *
     * @param estadoActual Estado instantáneo del proyectil.
     * @param configRebote Configuración con coeficiente de restitución, fricción y umbral de reposo.
     * @return Nuevo [ProjectileState] tras resolver el impacto (con rebote o en reposo definitivo).
     */
    fun resolverColision(
        estadoActual: ProjectileState,
        configRebote: BounceConfig
    ): ProjectileState
}
